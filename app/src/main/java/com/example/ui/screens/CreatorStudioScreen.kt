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
import com.example.model.ClothingCategory
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorStudioScreen(
    viewModel: AuraverseViewModel
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ClothingCategory.TOPS) }
    var priceGems by remember { mutableIntStateOf(50) }
    var selectedPattern by remember { mutableStateOf("Cyber Circuit") }
    var primaryColor by remember { mutableLongStateOf(0xFF7C3AED) }
    var secondaryColor by remember { mutableLongStateOf(0xFF06B6D4) }
    var accentColor by remember { mutableLongStateOf(0xFFF43F5E) }
    var publishSuccess by remember { mutableStateOf(false) }

    val patterns = listOf("Cyber Circuit", "Sakura Blossom", "Cosmic Nebula", "Aurora Pulse", "Starlight Grid")

    Scaffold(
        containerColor = AuraDeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CREATOR STUDIO",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AuraDarkSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraBorderGlow)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(AuraPurplePrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Rounded.Brush, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("DESIGN ORIGINAL ITEMS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Earn 50 💎 and Style Score upon publishing!", color = AuraCyanLight, fontSize = 11.sp)
                        }
                    }
                }
            }

            // 1. LIVE ITEM PREVIEW CARD
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(primaryColor)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(primaryColor).copy(alpha = 0.8f), Color(secondaryColor).copy(alpha = 0.6f))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = when (selectedCategory) {
                                    ClothingCategory.TOPS -> Icons.Rounded.Checkroom
                                    ClothingCategory.BOTTOMS -> Icons.Rounded.ShoppingBag
                                    ClothingCategory.OUTFITS -> Icons.Rounded.AutoAwesome
                                    else -> Icons.Rounded.Star
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = title.ifBlank { "Neon Anime Fit" },
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Pattern: $selectedPattern • ${selectedCategory.label}",
                                color = AuraCyanLight,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xAA0F172A)
                            ) {
                                Text(
                                    text = "$priceGems 💎",
                                    color = AuraGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. ITEM TITLE INPUT
            item {
                Text("ITEM TITLE", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("e.g. Cyberpunk Hologram Hoodie", color = AuraMutedSilver) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuraCyanSecondary,
                        unfocusedBorderColor = AuraElevatedSurface,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            // 3. CATEGORY SELECTOR
            item {
                Text("CATEGORY", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(ClothingCategory.TOPS, ClothingCategory.BOTTOMS, ClothingCategory.OUTFITS, ClothingCategory.SHOES).forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AuraPurplePrimary else AuraCardSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = cat.label,
                                color = if (isSelected) Color.White else AuraSilver,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            // 4. PATTERN SELECTOR
            item {
                Text("TEXTILE PATTERN", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(patterns) { pat ->
                        val isSelected = pat == selectedPattern
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AuraCyanSecondary else AuraCardSurface,
                            modifier = Modifier.clickable { selectedPattern = pat }
                        ) {
                            Text(
                                text = pat,
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // 5. COLOR PALETTES
            item {
                Text("PRIMARY COLOR", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CatalogData.HAIR_COLORS) { (hex, _) ->
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(hex))
                                .border(
                                    width = if (primaryColor == hex) 3.dp else 1.dp,
                                    color = if (primaryColor == hex) AuraCyanSecondary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { primaryColor = hex }
                        )
                    }
                }
            }

            item {
                Text("SECONDARY ACCENT COLOR", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CatalogData.EYE_COLORS) { (hex, _) ->
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(hex))
                                .border(
                                    width = if (secondaryColor == hex) 3.dp else 1.dp,
                                    color = if (secondaryColor == hex) AuraCyanSecondary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { secondaryColor = hex }
                        )
                    }
                }
            }

            // 6. PRICE SLIDER
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("MARKETPLACE PRICE (GEMS)", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("$priceGems 💎", color = AuraGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Slider(
                    value = priceGems.toFloat(),
                    onValueChange = { priceGems = it.toInt() },
                    valueRange = 10f..300f,
                    colors = SliderDefaults.colors(thumbColor = AuraGold, activeTrackColor = AuraPurplePrimary)
                )
            }

            // 7. PUBLISH BUTTON
            item {
                Button(
                    onClick = {
                        viewModel.publishCreatorItem(
                            title = title,
                            category = selectedCategory,
                            priceGems = priceGems,
                            pattern = selectedPattern,
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor,
                            accentColor = accentColor
                        )
                        publishSuccess = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("publish_item_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
                ) {
                    Icon(imageVector = Icons.Rounded.CloudUpload, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PUBLISH TO AURAVERSE MARKETPLACE", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }

    if (publishSuccess) {
        AlertDialog(
            onDismissRequest = {
                publishSuccess = false
                viewModel.navigateTo(ScreenRoute.MARKETPLACE)
            },
            containerColor = AuraDarkSurface,
            title = {
                Text("🎉 ITEM PUBLISHED!", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Your creation '$title' is now live on the Auraverse Marketplace! You earned +50 💎 and +100 Style Score.",
                    color = AuraSilver
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        publishSuccess = false
                        viewModel.navigateTo(ScreenRoute.MARKETPLACE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
                ) {
                    Text("VIEW MARKETPLACE")
                }
            }
        )
    }
}
