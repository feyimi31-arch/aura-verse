package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CatalogData
import com.example.graphics.Avatar3DRenderer
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

enum class AvatarCreatorTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    BODY("Body", Icons.Rounded.AccessibilityNew),
    FACE("Face", Icons.Rounded.Face),
    HAIR("Hair", Icons.Rounded.AutoAwesome),
    CLOTHES("Clothes", Icons.Rounded.Checkroom),
    SHOES("Shoes", Icons.Rounded.RollerSkating),
    ACCESSORIES("Accessories", Icons.Rounded.Stars)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarCreatorScreen(
    viewModel: AuraverseViewModel
) {
    val avatarConfig by viewModel.avatarConfig.collectAsState()
    var selectedTab by remember { mutableStateOf(AvatarCreatorTab.BODY) }
    var selectedHairCategory by remember { mutableStateOf(HairCategory.TWIN) }
    var selectedClothingCategory by remember { mutableStateOf(ClothingCategory.TOPS) }

    val poses = listOf("IDLE", "WALK", "RUN", "DANCE", "WAVE", "VICTORY", "SIT")

    Scaffold(
        containerColor = AuraDeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AVATAR CREATOR",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.randomizeAvatar() }) {
                        Icon(imageVector = Icons.Rounded.Shuffle, contentDescription = "Randomize", tint = AuraCyanSecondary)
                    }
                    IconButton(onClick = { viewModel.resetAvatar() }) {
                        Icon(imageVector = Icons.Rounded.RestartAlt, contentDescription = "Reset", tint = AuraMutedSilver)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AuraDarkSurface)
            )
        },
        bottomBar = {
            // Action Bar: SAVE & DONE
            Surface(
                color = AuraDarkSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.saveAvatar() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("avatar_save_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AuraPurpleLight)
                    ) {
                        Icon(imageVector = Icons.Rounded.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAVE LOOK", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.saveAvatar()
                            viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("avatar_done_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
                    ) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DONE", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. 3D AVATAR VIEWPORT (Center Interactive 3D Canvas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.95f)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E1B4B), AuraDarkSurface, AuraDeepNavy)
                        )
                    )
            ) {
                Avatar3DRenderer(
                    avatarConfig = avatarConfig,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("avatar_3d_viewport"),
                    isInteractive = true,
                    onRotationChange = { rot ->
                        viewModel.updateAvatarConfig(avatarConfig.copy(rotationY = rot))
                    }
                )

                // Hint overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp)
                        .background(Color(0x990F172A), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Drag to rotate 360°",
                        color = AuraCyanLight,
                        fontSize = 11.sp
                    )
                }

                // Pose selector chips floating at bottom-right of 3D canvas
                LazyRow(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(poses) { pose ->
                        val isSelected = avatarConfig.currentPose == pose
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) AuraPurplePrimary else Color(0xAA0F172A),
                            modifier = Modifier
                                .clickable { viewModel.setAvatarPose(pose) }
                        ) {
                            Text(
                                text = pose,
                                color = if (isSelected) Color.White else AuraSilver,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // 2. CATEGORY TABS
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = AuraDarkSurface,
                contentColor = AuraPurpleLight,
                edgePadding = 12.dp,
                divider = {}
            ) {
                AvatarCreatorTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tab.label, modifier = Modifier.size(18.dp)) },
                        selectedContentColor = AuraCyanSecondary,
                        unselectedContentColor = AuraMutedSilver
                    )
                }
            }

            // 3. TAB CONTENT EDITOR PANEL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.05f)
                    .background(AuraCardSurface)
            ) {
                when (selectedTab) {
                    AvatarCreatorTab.BODY -> BodyCustomizerTab(avatarConfig, viewModel)
                    AvatarCreatorTab.FACE -> FaceCustomizerTab(avatarConfig, viewModel)
                    AvatarCreatorTab.HAIR -> HairCustomizerTab(avatarConfig, selectedHairCategory, { selectedHairCategory = it }, viewModel)
                    AvatarCreatorTab.CLOTHES -> ClothesCustomizerTab(avatarConfig, selectedClothingCategory, { selectedClothingCategory = it }, viewModel)
                    AvatarCreatorTab.SHOES -> ShoesCustomizerTab(avatarConfig, viewModel)
                    AvatarCreatorTab.ACCESSORIES -> AccessoriesCustomizerTab(avatarConfig, viewModel)
                }
            }
        }
    }
}

// ----------------------------------------------------
// BODY CUSTOMIZER TAB
// ----------------------------------------------------
@Composable
fun BodyCustomizerTab(config: AvatarConfig, viewModel: AuraverseViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Gender Templates
        item {
            Text("BODY TEMPLATE", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GenderTemplate.entries.forEach { template ->
                    val isSelected = config.template == template
                    OutlinedButton(
                        onClick = { viewModel.updateAvatarConfig(config.copy(template = template)) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) AuraPurplePrimary else Color.Transparent,
                            contentColor = if (isSelected) Color.White else AuraSilver
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(template.label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Body Presets
        item {
            Text("BODY PRESETS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(BodyPreset.entries) { preset ->
                    val isSelected = config.preset == preset
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraElevatedSurface,
                        modifier = Modifier.clickable { viewModel.setBodyPreset(preset) }
                    ) {
                        Text(
                            text = preset.label,
                            color = if (isSelected) Color.White else AuraSilver,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Proportion Sliders
        item {
            Text("FINE TUNE PROPORTIONS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            ProportionSlider("Height", config.proportions.height, 0.8f..1.25f) {
                viewModel.updateProportions(config.proportions.copy(height = it))
            }
            ProportionSlider("Body Width", config.proportions.bodyWidth, 0.8f..1.3f) {
                viewModel.updateProportions(config.proportions.copy(bodyWidth = it))
            }
            ProportionSlider("Shoulder Width", config.proportions.shoulderWidth, 0.8f..1.35f) {
                viewModel.updateProportions(config.proportions.copy(shoulderWidth = it))
            }
            ProportionSlider("Torso Width", config.proportions.torsoWidth, 0.8f..1.3f) {
                viewModel.updateProportions(config.proportions.copy(torsoWidth = it))
            }
            ProportionSlider("Arm Length", config.proportions.armLength, 0.85f..1.2f) {
                viewModel.updateProportions(config.proportions.copy(armLength = it))
            }
            ProportionSlider("Leg Length", config.proportions.legLength, 0.85f..1.25f) {
                viewModel.updateProportions(config.proportions.copy(legLength = it))
            }
        }
    }
}

@Composable
fun ProportionSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = AuraSilver, fontSize = 12.sp)
            Text(String.format("%.2f", value), color = AuraCyanLight, fontSize = 12.sp)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = AuraCyanSecondary,
                activeTrackColor = AuraPurplePrimary,
                inactiveTrackColor = AuraElevatedSurface
            )
        )
    }
}

// ----------------------------------------------------
// FACE CUSTOMIZER TAB
// ----------------------------------------------------
@Composable
fun FaceCustomizerTab(config: AvatarConfig, viewModel: AuraverseViewModel) {
    val face = config.face

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Skin Tones
        item {
            Text("SKIN TONE", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CatalogData.SKIN_TONES) { (toneHex, name) ->
                    val isSelected = face.skinTone == toneHex
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(toneHex))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) AuraCyanSecondary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.updateFace(face.copy(skinTone = toneHex)) }
                    )
                }
            }
        }

        // Eye Shapes
        item {
            Text("EYE SHAPE", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(EyeShape.entries) { shape ->
                    val isSelected = face.eyeShape == shape
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraElevatedSurface,
                        modifier = Modifier.clickable { viewModel.updateFace(face.copy(eyeShape = shape)) }
                    ) {
                        Text(
                            text = shape.label,
                            color = if (isSelected) Color.White else AuraSilver,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Eye Colors
        item {
            Text("EYE COLOR", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CatalogData.EYE_COLORS) { (colorHex, name) ->
                    val isSelected = face.eyeColor == colorHex
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(colorHex))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) AuraCyanSecondary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.updateFace(face.copy(eyeColor = colorHex)) }
                    )
                }
            }
        }

        // Mouth Styles
        item {
            Text("MOUTH EXPRESSION", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(MouthStyle.entries) { mouth ->
                    val isSelected = face.mouthStyle == mouth
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraElevatedSurface,
                        modifier = Modifier.clickable { viewModel.updateFace(face.copy(mouthStyle = mouth)) }
                    ) {
                        Text(
                            text = mouth.label,
                            color = if (isSelected) Color.White else AuraSilver,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Blush & Details
        item {
            Text("BLUSH & DETAILS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(BlushStyle.entries) { blush ->
                    val isSelected = face.blushStyle == blush
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraElevatedSurface,
                        modifier = Modifier.clickable { viewModel.updateFace(face.copy(blushStyle = blush)) }
                    ) {
                        Text(
                            text = blush.label,
                            color = if (isSelected) Color.White else AuraSilver,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// HAIR CUSTOMIZER TAB
// ----------------------------------------------------
@Composable
fun HairCustomizerTab(
    config: AvatarConfig,
    category: HairCategory,
    onCategoryChange: (HairCategory) -> Unit,
    viewModel: AuraverseViewModel
) {
    val filteredHair = CatalogData.HAIRSTYLES.filter { it.category == category }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Category Pills
        item {
            Text("HAIR CATEGORY", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(HairCategory.entries) { cat ->
                    val isSelected = cat == category
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraElevatedSurface,
                        modifier = Modifier.clickable { onCategoryChange(cat) }
                    ) {
                        Text(
                            text = cat.label,
                            color = if (isSelected) Color.White else AuraSilver,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Hairstyle Selection
        item {
            Text("HAIRSTYLES (${category.label})", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredHair) { hair ->
                    val isSelected = config.hair.id == hair.id
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) AuraElevatedSurface else AuraDarkSurface
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, AuraCyanSecondary) else null,
                        modifier = Modifier
                            .width(130.dp)
                            .clickable {
                                viewModel.setHairstyle(
                                    hair.copy(
                                        primaryColor = config.hair.primaryColor,
                                        secondaryColor = config.hair.secondaryColor,
                                        highlightColor = config.hair.highlightColor
                                    )
                                )
                            }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(
                                        Brush.verticalGradient(listOf(Color(hair.primaryColor), Color(hair.secondaryColor))),
                                        RoundedCornerShape(8.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = hair.name,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Primary Hair Color
        item {
            Text("PRIMARY COLOR", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CatalogData.HAIR_COLORS) { (colorHex, name) ->
                    val isSelected = config.hair.primaryColor == colorHex
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(colorHex))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) AuraCyanSecondary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.setHairColors(
                                    primary = colorHex,
                                    secondary = config.hair.secondaryColor,
                                    highlight = config.hair.highlightColor
                                )
                            }
                    )
                }
            }
        }

        // Secondary Ombré Tips Color
        item {
            Text("SECONDARY / OMBRÉ TIPS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CatalogData.HAIR_COLORS) { (colorHex, name) ->
                    val isSelected = config.hair.secondaryColor == colorHex
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(colorHex))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) AuraCyanSecondary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.setHairColors(
                                    primary = config.hair.primaryColor,
                                    secondary = colorHex,
                                    highlight = config.hair.highlightColor
                                )
                            }
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// CLOTHES CUSTOMIZER TAB
// ----------------------------------------------------
@Composable
fun ClothesCustomizerTab(
    config: AvatarConfig,
    category: ClothingCategory,
    onCategoryChange: (ClothingCategory) -> Unit,
    viewModel: AuraverseViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Clothing Category Toggle (Tops, Bottoms, Full Outfits)
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(ClothingCategory.TOPS, ClothingCategory.BOTTOMS, ClothingCategory.OUTFITS).forEach { cat ->
                    val isSelected = cat == category
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraElevatedSurface,
                        modifier = Modifier.clickable { onCategoryChange(cat) }
                    ) {
                        Text(
                            text = cat.label,
                            color = if (isSelected) Color.White else AuraSilver,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Items list
        item {
            val itemsList = when (category) {
                ClothingCategory.TOPS -> CatalogData.TOPS
                ClothingCategory.BOTTOMS -> CatalogData.BOTTOMS
                ClothingCategory.OUTFITS -> CatalogData.FULL_OUTFITS
                else -> emptyList()
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(itemsList) { item ->
                    val isEquipped = when (category) {
                        ClothingCategory.TOPS -> config.top.id == item.id
                        ClothingCategory.BOTTOMS -> config.bottom.id == item.id
                        ClothingCategory.OUTFITS -> config.fullOutfit?.id == item.id
                        else -> false
                    }
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isEquipped) AuraElevatedSurface else AuraDarkSurface
                        ),
                        border = if (isEquipped) androidx.compose.foundation.BorderStroke(2.dp, AuraCyanSecondary) else null,
                        modifier = Modifier
                            .width(135.dp)
                            .clickable {
                                when (category) {
                                    ClothingCategory.TOPS -> viewModel.setTop(item)
                                    ClothingCategory.BOTTOMS -> viewModel.setBottom(item)
                                    ClothingCategory.OUTFITS -> viewModel.setFullOutfit(item)
                                    else -> {}
                                }
                            }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(
                                        Brush.linearGradient(listOf(Color(item.primaryColor), Color(item.secondaryColor))),
                                        RoundedCornerShape(8.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.name,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// SHOES CUSTOMIZER TAB
// ----------------------------------------------------
@Composable
fun ShoesCustomizerTab(config: AvatarConfig, viewModel: AuraverseViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("FOOTWEAR", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CatalogData.SHOES) { shoe ->
                    val isEquipped = config.shoes.id == shoe.id
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isEquipped) AuraElevatedSurface else AuraDarkSurface
                        ),
                        border = if (isEquipped) androidx.compose.foundation.BorderStroke(2.dp, AuraCyanSecondary) else null,
                        modifier = Modifier
                            .width(135.dp)
                            .clickable { viewModel.setShoes(shoe) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(
                                        Brush.horizontalGradient(listOf(Color(shoe.primaryColor), Color(shoe.secondaryColor))),
                                        RoundedCornerShape(8.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = shoe.name,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// ACCESSORIES CUSTOMIZER TAB
// ----------------------------------------------------
@Composable
fun AccessoriesCustomizerTab(config: AvatarConfig, viewModel: AuraverseViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("ACCESSORIES & AURA FX", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CatalogData.ACCESSORIES) { acc ->
                    val isEquipped = config.equippedAccessories.any { it.id == acc.id }
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isEquipped) AuraElevatedSurface else AuraDarkSurface
                        ),
                        border = if (isEquipped) androidx.compose.foundation.BorderStroke(2.dp, AuraCyanSecondary) else null,
                        modifier = Modifier
                            .width(140.dp)
                            .clickable { viewModel.toggleAccessory(acc) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(
                                        Brush.radialGradient(listOf(Color(acc.color), Color(acc.secondaryColor))),
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (isEquipped) "EQUIPPED" else "TAP TO WEAR", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = acc.name,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = acc.slot.label,
                                color = AuraMutedSilver,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
