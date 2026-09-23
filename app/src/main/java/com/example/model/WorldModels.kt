package com.example.model

enum class DistrictId(val label: String) {
    AURA_CITY("Aura City"),
    AURA_ACADEMY("Aura Academy"),
    AURA_BEACH("Aura Beach"),
    AURA_AMUSEMENT("Aura Amusement"),
    MYSTIC_FOREST("Mystic Forest"),
    FANTASY_REALM("Fantasy Realm")
}

data class District(
    val id: DistrictId,
    val name: String,
    val tagline: String,
    val description: String,
    val themeColor: Long,
    val skyboxColorTop: Long,
    val skyboxColorBottom: Long,
    val groundColor: Long,
    val landmarks: List<String>,
    val activePlayersCount: Int = 142
)

data class NPC(
    val id: String,
    val name: String,
    val role: String,
    val districtId: DistrictId,
    val greeting: String,
    val dialogues: List<String>,
    val questName: String? = null,
    val questDesc: String? = null,
    val questRewardCoins: Int = 200,
    val questRewardGems: Int = 15,
    val posX: Float = 0f,
    val posZ: Float = 0f
)

enum class VehicleType(val label: String) {
    HOVERBOARD("Cyber Hoverboard"),
    SPORT_BIKE("Neon Sports Bike"),
    SCOOTER("City Retro Scooter"),
    CRUISER("Futuristic Aero Cruiser")
}

data class Vehicle(
    val id: String,
    val name: String,
    val type: VehicleType,
    val topSpeed: Float,
    val acceleration: Float,
    val neonColor: Long,
    val priceGems: Int,
    val isUnlocked: Boolean = false
)

enum class PetSpecies(val label: String) {
    CYBER_KITSUNE("Cyber Kitsune"),
    CELESTIAL_BUNNY("Celestial Bunny"),
    NEON_SHIBA("Neon Shiba Inu"),
    STAR_DRAGONLING("Star Dragonling"),
    MECHA_CAT("Mecha Neko")
}

data class Pet(
    val id: String,
    var customName: String,
    val species: PetSpecies,
    val primaryColor: Long,
    val accentColor: Long,
    var level: Int = 1,
    var happiness: Int = 100, // 0 - 100
    val trick: String = "Spin & Star Heart",
    val isUnlocked: Boolean = false
)

data class WorldPlayerState(
    var posX: Float = 0f,
    var posY: Float = 0f,
    var posZ: Float = 0f,
    var rotationY: Float = 0f,
    var isMoving: Boolean = false,
    var isRunning: Boolean = false,
    var isJumping: Boolean = false,
    var isSitting: Boolean = false,
    var isMounted: Boolean = false,
    var currentEmote: String? = null
)
