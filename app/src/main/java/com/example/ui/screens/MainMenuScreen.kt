package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CatalogData
import com.example.model.District
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

@Composable
fun MainMenuScreen(
    viewModel: AuraverseViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val activePet by viewModel.activePet.collectAsState()
    val currentDistrict by viewModel.currentDistrict.collectAsState()

    Scaffold(
        containerColor = AuraDeepNavy
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. TOP HEADER & STATUS BAR
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(AuraPurplePrimary, AuraCyanSecondary)))
                                .border(2.dp, AuraBorderGlow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.username.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.username,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(AuraPurpleDark, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Lv.${profile.level}",
                                        color = AuraPurpleLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = profile.title,
                                color = AuraMutedSilver,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Currency Pills
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Coins
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = AuraCardSurface,
                            border = ButtonDefaults.outlinedButtonBorder
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.MonetizationOn,
                                    contentDescription = "Aura Coins",
                                    tint = AuraGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${profile.coins}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Gems
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = AuraCardSurface,
                            border = ButtonDefaults.outlinedButtonBorder
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Diamond,
                                    contentDescription = "Aura Gems",
                                    tint = AuraCyanGlow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${profile.gems}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // 2. HERO BANNER
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .testTag("hero_banner_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraDarkSurface)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.auraverse_hero),
                            contentDescription = "Auraverse Universe",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0x99070A14), Color(0xFA070A14))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "AURAVERSE",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "Create Your Avatar. Explore Your World. Live Your Story.",
                                color = AuraCyanLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // 3. PRIMARY ACTION: PLAY / ENTER 3D WORLD
            item {
                Button(
                    onClick = { viewModel.navigateTo(ScreenRoute.WORLD) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("play_world_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Explore,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "EXPLORE OPEN WORLD",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // 4. QUICK ACCESS GRID TILES
            item {
                Text(
                    text = "ACTIVITIES & FEATURES",
                    color = AuraMutedSilver,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MenuTile(
                        title = "Avatar Studio",
                        subtitle = "Customize 3D Look",
                        icon = Icons.Rounded.Face,
                        accentColor = AuraPurplePrimary,
                        modifier = Modifier.weight(1f),
                        testTag = "menu_avatar_creator"
                    ) {
                        viewModel.navigateTo(ScreenRoute.AVATAR_CREATOR)
                    }

                    MenuTile(
                        title = "Mini-Games",
                        subtitle = "Obby & Rhythm Beat",
                        icon = Icons.Rounded.SportsEsports,
                        accentColor = AuraPinkAccent,
                        modifier = Modifier.weight(1f),
                        testTag = "menu_mini_games"
                    ) {
                        viewModel.navigateTo(ScreenRoute.MINI_GAMES)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MenuTile(
                        title = "Creator Studio",
                        subtitle = "Design & Publish",
                        icon = Icons.Rounded.Brush,
                        accentColor = AuraCyanSecondary,
                        modifier = Modifier.weight(1f),
                        testTag = "menu_creator_studio"
                    ) {
                        viewModel.navigateTo(ScreenRoute.CREATOR_STUDIO)
                    }

                    MenuTile(
                        title = "Marketplace",
                        subtitle = "Shop Anime Fits",
                        icon = Icons.Rounded.ShoppingBag,
                        accentColor = AuraGold,
                        modifier = Modifier.weight(1f),
                        testTag = "menu_marketplace"
                    ) {
                        viewModel.navigateTo(ScreenRoute.MARKETPLACE)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MenuTile(
                        title = "Inventory",
                        subtitle = "Wardrobe & Pets",
                        icon = Icons.Rounded.Backpack,
                        accentColor = AuraPurpleLight,
                        modifier = Modifier.weight(1f),
                        testTag = "menu_inventory"
                    ) {
                        viewModel.navigateTo(ScreenRoute.INVENTORY)
                    }

                    MenuTile(
                        title = "Social Hub",
                        subtitle = "Friends & Chat",
                        icon = Icons.Rounded.People,
                        accentColor = AuraEmerald,
                        modifier = Modifier.weight(1f),
                        testTag = "menu_social"
                    ) {
                        viewModel.navigateTo(ScreenRoute.SOCIAL)
                    }
                }
            }

            // 5. DISTRICT EXPLORATION CAROUSEL
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPLORE DISTRICTS",
                        color = AuraMutedSilver,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "6 Realms",
                        color = AuraCyanLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(CatalogData.DISTRICTS) { district ->
                        DistrictPreviewCard(
                            district = district,
                            isSelected = district.id == currentDistrict.id
                        ) {
                            viewModel.selectDistrict(district)
                            viewModel.navigateTo(ScreenRoute.WORLD)
                        }
                    }
                }
            }

            // 6. SETTINGS & INFO
            item {
                OutlinedButton(
                    onClick = { viewModel.navigateTo(ScreenRoute.SETTINGS) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("menu_settings_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AuraSilver)
                ) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Game Settings & Safety Controls", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun MenuTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AuraCardSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    color = AuraMutedSilver,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun DistrictPreviewCard(
    district: District,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AuraElevatedSurface else AuraCardSurface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(district.themeColor)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(district.themeColor), CircleShape)
                )
                Text(
                    text = "${district.activePlayersCount} online",
                    color = AuraCyanLight,
                    fontSize = 10.sp
                )
            }

            Column {
                Text(
                    text = district.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = district.tagline,
                    color = AuraMutedSilver,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
