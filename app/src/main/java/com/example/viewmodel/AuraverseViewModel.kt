package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.sqrt

enum class ScreenRoute {
    MAIN_MENU,
    WORLD,
    AVATAR_CREATOR,
    INVENTORY,
    MARKETPLACE,
    MINI_GAMES,
    CREATOR_STUDIO,
    SOCIAL,
    SETTINGS
}

class AuraverseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlayerRepository by lazy {
        val db = AuraverseDatabase.getDatabase(application)
        PlayerRepository(db.auraverseDao())
    }

    // Navigation & Screen State
    private val _currentScreen = MutableStateFlow(ScreenRoute.MAIN_MENU)
    val currentScreen: StateFlow<ScreenRoute> = _currentScreen.asStateFlow()

    // Avatar Configuration
    private val _avatarConfig = MutableStateFlow(AvatarConfig())
    val avatarConfig: StateFlow<AvatarConfig> = _avatarConfig.asStateFlow()

    private var initialAvatarConfig = AvatarConfig()

    // Player Profile & Economy
    private val _profile = MutableStateFlow(PlayerProfile())
    val profile: StateFlow<PlayerProfile> = _profile.asStateFlow()

    // World & District
    private val _currentDistrict = MutableStateFlow(CatalogData.DISTRICTS[0])
    val currentDistrict: StateFlow<District> = _currentDistrict.asStateFlow()

    private val _playerState = MutableStateFlow(WorldPlayerState())
    val playerState: StateFlow<WorldPlayerState> = _playerState.asStateFlow()

    // Vehicles & Pets
    private val _activeVehicle = MutableStateFlow<Vehicle?>(CatalogData.VEHICLES[0])
    val activeVehicle: StateFlow<Vehicle?> = _activeVehicle.asStateFlow()

    private val _activePet = MutableStateFlow<Pet?>(CatalogData.PETS[0])
    val activePet: StateFlow<Pet?> = _activePet.asStateFlow()

    private val _allVehicles = MutableStateFlow(CatalogData.VEHICLES)
    val allVehicles: StateFlow<List<Vehicle>> = _allVehicles.asStateFlow()

    private val _allPets = MutableStateFlow(CatalogData.PETS)
    val allPets: StateFlow<List<Pet>> = _allPets.asStateFlow()

    // Inventory & Creator items
    private val _ownedItemIds = MutableStateFlow(
        setOf("top_cyber_hoodie", "top_academy_blazer", "bot_pleated_skirt", "bot_cargo_pants", "shoe_platform_sneakers", "acc_cat_headphones", "acc_angel_wings")
    )
    val ownedItemIds: StateFlow<Set<String>> = _ownedItemIds.asStateFlow()

    private val _marketplaceItems = MutableStateFlow(CatalogData.COMMUNITY_MARKETPLACE)
    val marketplaceItems: StateFlow<List<CreatorItem>> = _marketplaceItems.asStateFlow()

    // Social & Chat
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("1", "AuraSystem", "Welcome to AURAVERSE! Explore districts, design styles, and hang out with friends.", ChatChannel.DISTRICT, "1m ago", true),
            ChatMessage("2", "Kaito", "Drop by Fashion Boulevard in Aura City to check the new neon fits!", ChatChannel.DISTRICT, "Just now", false, 0xFF06B6D4),
            ChatMessage("3", "Celeste", "Academy Sports Field obby tournament begins soon! ⭐", ChatChannel.GLOBAL, "Just now", false, 0xFF8B5CF6)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _activeNpcDialogue = MutableStateFlow<NPC?>(null)
    val activeNpcDialogue: StateFlow<NPC?> = _activeNpcDialogue.asStateFlow()

    // Mini-Game State
    private val _activeMiniGame = MutableStateFlow<MiniGameType?>(null)
    val activeMiniGame: StateFlow<MiniGameType?> = _activeMiniGame.asStateFlow()

    private val _miniGameScore = MutableStateFlow(0)
    val miniGameScore: StateFlow<Int> = _miniGameScore.asStateFlow()

    // Jump Physics Coroutine
    private var jumpJob: Job? = null

    init {
        // Load saved state from repository
        viewModelScope.launch {
            repository.playerProfile.collect { entity ->
                if (entity != null) {
                    _profile.value = _profile.value.copy(
                        username = entity.username,
                        title = entity.title,
                        bio = entity.bio,
                        level = entity.level,
                        styleScore = entity.styleScore,
                        coins = entity.coins,
                        gems = entity.gems
                    )
                }
            }
        }
        initialAvatarConfig = _avatarConfig.value
    }

    // ----------------------------------------------------
    // NAVIGATION
    // ----------------------------------------------------
    fun navigateTo(route: ScreenRoute) {
        _currentScreen.value = route
        if (route == ScreenRoute.AVATAR_CREATOR) {
            initialAvatarConfig = _avatarConfig.value
        }
    }

    // ----------------------------------------------------
    // AVATAR CUSTOMIZATION
    // ----------------------------------------------------
    fun updateAvatarConfig(newConfig: AvatarConfig) {
        _avatarConfig.value = newConfig
    }

    fun setBodyPreset(preset: BodyPreset) {
        val proportions = CatalogData.PRESET_PROPORTIONS[preset] ?: BodyProportions()
        _avatarConfig.value = _avatarConfig.value.copy(
            preset = preset,
            proportions = proportions
        )
    }

    fun updateProportions(newProportions: BodyProportions) {
        _avatarConfig.value = _avatarConfig.value.copy(proportions = newProportions)
    }

    fun updateFace(newFace: FaceFeatures) {
        _avatarConfig.value = _avatarConfig.value.copy(face = newFace)
    }

    fun setHairstyle(hair: Hairstyle) {
        _avatarConfig.value = _avatarConfig.value.copy(hair = hair)
    }

    fun setHairColors(primary: Long, secondary: Long, highlight: Long) {
        val currentHair = _avatarConfig.value.hair
        _avatarConfig.value = _avatarConfig.value.copy(
            hair = currentHair.copy(primaryColor = primary, secondaryColor = secondary, highlightColor = highlight)
        )
    }

    fun setTop(item: ClothingItem) {
        _avatarConfig.value = _avatarConfig.value.copy(top = item, fullOutfit = null)
    }

    fun setBottom(item: ClothingItem) {
        _avatarConfig.value = _avatarConfig.value.copy(bottom = item, fullOutfit = null)
    }

    fun setFullOutfit(item: ClothingItem) {
        _avatarConfig.value = _avatarConfig.value.copy(fullOutfit = item)
    }

    fun setShoes(item: ClothingItem) {
        _avatarConfig.value = _avatarConfig.value.copy(shoes = item)
    }

    fun toggleAccessory(acc: AccessoryItem) {
        val currentList = _avatarConfig.value.equippedAccessories.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == acc.id }
        if (existingIndex >= 0) {
            currentList.removeAt(existingIndex)
        } else {
            // Replace same slot or add
            currentList.removeAll { it.slot == acc.slot }
            currentList.add(acc)
        }
        _avatarConfig.value = _avatarConfig.value.copy(equippedAccessories = currentList)
    }

    fun setAvatarPose(pose: String) {
        _avatarConfig.value = _avatarConfig.value.copy(currentPose = pose)
    }

    fun randomizeAvatar() {
        val randomSkin = CatalogData.SKIN_TONES.random().first
        val randomEyeColor = CatalogData.EYE_COLORS.random().first
        val randomHairColor = CatalogData.HAIR_COLORS.random().first
        val randomHairSec = CatalogData.HAIR_COLORS.random().first
        val randomHair = CatalogData.HAIRSTYLES.random().copy(
            primaryColor = randomHairColor,
            secondaryColor = randomHairSec
        )
        val randomTop = CatalogData.TOPS.random()
        val randomBottom = CatalogData.BOTTOMS.random()
        val randomShoes = CatalogData.SHOES.random()

        _avatarConfig.value = _avatarConfig.value.copy(
            face = _avatarConfig.value.face.copy(
                skinTone = randomSkin,
                eyeColor = randomEyeColor,
                eyeShape = EyeShape.entries.random(),
                mouthStyle = MouthStyle.entries.random()
            ),
            hair = randomHair,
            top = randomTop,
            bottom = randomBottom,
            shoes = randomShoes
        )
    }

    fun resetAvatar() {
        _avatarConfig.value = initialAvatarConfig
    }

    fun saveAvatar() {
        initialAvatarConfig = _avatarConfig.value
        viewModelScope.launch {
            repository.savePlayer(
                PlayerEntity(
                    username = _profile.value.username,
                    title = _profile.value.title,
                    bio = _profile.value.bio,
                    level = _profile.value.level,
                    styleScore = _profile.value.styleScore + 50,
                    coins = _profile.value.coins,
                    gems = _profile.value.gems
                )
            )
        }
        _profile.value = _profile.value.copy(styleScore = _profile.value.styleScore + 50)
    }

    // ----------------------------------------------------
    // WORLD EXPLORATION & MOVEMENT
    // ----------------------------------------------------
    fun selectDistrict(district: District) {
        _currentDistrict.value = district
        _playerState.value = _playerState.value.copy(posX = 0f, posY = 0f, posZ = 0f)
        _activeNpcDialogue.value = null
    }

    fun movePlayer(joystickX: Float, joystickY: Float, isTurbo: Boolean) {
        if (abs(joystickX) < 0.05f && abs(joystickY) < 0.05f) {
            _playerState.value = _playerState.value.copy(isMoving = false)
            _avatarConfig.value = _avatarConfig.value.copy(currentPose = "IDLE")
            return
        }

        val speed = when {
            _playerState.value.isMounted -> 2.2f * (_activeVehicle.value?.topSpeed ?: 1.5f)
            isTurbo -> 1.8f
            else -> 1.0f
        }

        val targetAngle = Math.toDegrees(atan2(joystickX.toDouble(), -joystickY.toDouble())).toFloat()

        val newX = _playerState.value.posX + joystickX * speed * 2.5f
        val newZ = _playerState.value.posZ - joystickY * speed * 2.5f

        _playerState.value = _playerState.value.copy(
            posX = newX.coerceIn(-120f, 120f),
            posZ = newZ.coerceIn(-120f, 120f),
            rotationY = targetAngle,
            isMoving = true,
            isRunning = isTurbo
        )

        _avatarConfig.value = _avatarConfig.value.copy(
            currentPose = if (isTurbo) "RUN" else "WALK"
        )

        // Check proximity to NPCs
        val npcsInDistrict = CatalogData.NPCS.filter { it.districtId == _currentDistrict.value.id }
        var foundNearNpc: NPC? = null
        for (npc in npcsInDistrict) {
            val dist = sqrt((newX - npc.posX) * (newX - npc.posX) + (newZ - npc.posZ) * (newZ - npc.posZ))
            if (dist < 18f) {
                foundNearNpc = npc
                break
            }
        }
        _activeNpcDialogue.value = foundNearNpc
    }

    fun jump() {
        if (_playerState.value.isJumping) return
        jumpJob?.cancel()
        jumpJob = viewModelScope.launch {
            _playerState.value = _playerState.value.copy(isJumping = true)
            // Arc up
            for (step in 1..10) {
                val y = sin(step * Math.PI / 10f).toFloat() * 25f
                _playerState.value = _playerState.value.copy(posY = y)
                delay(30)
            }
            _playerState.value = _playerState.value.copy(posY = 0f, isJumping = false)
        }
    }

    fun toggleVehicleMount() {
        _playerState.value = _playerState.value.copy(
            isMounted = !_playerState.value.isMounted
        )
    }

    fun playEmote(emote: EmoteAction) {
        _playerState.value = _playerState.value.copy(currentEmote = emote.name)
        _avatarConfig.value = _avatarConfig.value.copy(currentPose = emote.poseKey)
        viewModelScope.launch {
            delay(4000)
            if (_avatarConfig.value.currentPose == emote.poseKey) {
                _avatarConfig.value = _avatarConfig.value.copy(currentPose = "IDLE")
                _playerState.value = _playerState.value.copy(currentEmote = null)
            }
        }
    }

    fun completeNpcQuest(npc: NPC) {
        _profile.value = _profile.value.copy(
            coins = _profile.value.coins + npc.questRewardCoins,
            gems = _profile.value.gems + npc.questRewardGems
        )
        viewModelScope.launch {
            repository.updateCurrency(_profile.value.coins, _profile.value.gems)
        }
        _activeNpcDialogue.value = null
    }

    fun closeNpcDialogue() {
        _activeNpcDialogue.value = null
    }

    // ----------------------------------------------------
    // ECONOMY & MARKETPLACE
    // ----------------------------------------------------
    fun buyItem(item: CreatorItem) {
        if (_profile.value.gems >= item.priceGems && !_ownedItemIds.value.contains(item.id)) {
            val updatedGems = _profile.value.gems - item.priceGems
            _profile.value = _profile.value.copy(gems = updatedGems)
            _ownedItemIds.value = _ownedItemIds.value + item.id

            // Mark owned
            _marketplaceItems.value = _marketplaceItems.value.map {
                if (it.id == item.id) it.copy(isOwned = true) else it
            }

            viewModelScope.launch {
                repository.updateCurrency(_profile.value.coins, updatedGems)
            }
        }
    }

    fun publishCreatorItem(
        title: String,
        category: ClothingCategory,
        priceGems: Int,
        pattern: String,
        primaryColor: Long,
        secondaryColor: Long,
        accentColor: Long
    ) {
        val newItem = CreatorItem(
            id = "user_item_${System.currentTimeMillis()}",
            title = title.ifBlank { "Custom Starlight Fit" },
            creatorName = _profile.value.username,
            category = category,
            priceGems = priceGems.coerceIn(10, 500),
            patternName = pattern,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            accentColor = accentColor,
            likesCount = 1,
            isOfficial = false,
            isOwned = true
        )
        _marketplaceItems.value = listOf(newItem) + _marketplaceItems.value
        _ownedItemIds.value = _ownedItemIds.value + newItem.id
        _profile.value = _profile.value.copy(
            gems = _profile.value.gems + 50, // Reward for creating!
            styleScore = _profile.value.styleScore + 100
        )
    }

    // ----------------------------------------------------
    // PETS & VEHICLES SELECTION
    // ----------------------------------------------------
    fun selectPet(pet: Pet) {
        _activePet.value = pet
    }

    fun selectVehicle(vehicle: Vehicle) {
        _activeVehicle.value = vehicle
    }

    // ----------------------------------------------------
    // CHAT & SOCIAL
    // ----------------------------------------------------
    fun sendChatMessage(text: String, channel: ChatChannel) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            sender = _profile.value.username,
            text = text,
            channel = channel,
            timestamp = "Just now",
            senderColor = 0xFF8B5CF6
        )
        _chatMessages.value = _chatMessages.value + newMsg
    }

    // ----------------------------------------------------
    // MINI-GAMES
    // ----------------------------------------------------
    fun startMiniGame(type: MiniGameType) {
        _activeMiniGame.value = type
        _miniGameScore.value = 0
    }

    fun finishMiniGame(score: Int) {
        val game = _activeMiniGame.value ?: return
        val bonus = (score / 10).coerceAtLeast(1)
        val earnedGems = game.rewardGems + bonus
        val earnedCoins = 150 + bonus * 10

        _profile.value = _profile.value.copy(
            coins = _profile.value.coins + earnedCoins,
            gems = _profile.value.gems + earnedGems,
            styleScore = _profile.value.styleScore + 30
        )
        _activeMiniGame.value = null
        viewModelScope.launch {
            repository.updateCurrency(_profile.value.coins, _profile.value.gems)
        }
    }

    fun exitMiniGame() {
        _activeMiniGame.value = null
    }

    fun incrementMiniGameScore(points: Int) {
        _miniGameScore.value += points
    }
}
