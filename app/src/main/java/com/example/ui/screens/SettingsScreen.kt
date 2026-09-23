package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AuraverseViewModel
) {
    var musicVolume by remember { mutableFloatStateOf(0.8f) }
    var sfxVolume by remember { mutableFloatStateOf(0.9f) }
    var cameraSensitivity by remember { mutableFloatStateOf(1.0f) }
    var safeChatEnabled by remember { mutableStateOf(true) }
    var highFpsMode by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = AuraDeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SETTINGS",
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
            // Audio Section
            item {
                Text("AUDIO & SOUND", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraCardSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Music Volume", color = Color.White, fontSize = 13.sp)
                            Text("${(musicVolume * 100).toInt()}%", color = AuraCyanLight, fontSize = 13.sp)
                        }
                        Slider(
                            value = musicVolume,
                            onValueChange = { musicVolume = it },
                            colors = SliderDefaults.colors(thumbColor = AuraCyanSecondary, activeTrackColor = AuraPurplePrimary)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Sound Effects (SFX)", color = Color.White, fontSize = 13.sp)
                            Text("${(sfxVolume * 100).toInt()}%", color = AuraCyanLight, fontSize = 13.sp)
                        }
                        Slider(
                            value = sfxVolume,
                            onValueChange = { sfxVolume = it },
                            colors = SliderDefaults.colors(thumbColor = AuraCyanSecondary, activeTrackColor = AuraPurplePrimary)
                        )
                    }
                }
            }

            // Controls & Camera Section
            item {
                Text("CONTROLS & CAMERA", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraCardSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Camera Orbit Sensitivity", color = Color.White, fontSize = 13.sp)
                            Text("${(cameraSensitivity * 100).toInt()}%", color = AuraCyanLight, fontSize = 13.sp)
                        }
                        Slider(
                            value = cameraSensitivity,
                            onValueChange = { cameraSensitivity = it },
                            valueRange = 0.5f..2.0f,
                            colors = SliderDefaults.colors(thumbColor = AuraCyanSecondary, activeTrackColor = AuraPurplePrimary)
                        )
                    }
                }
            }

            // Graphics Section
            item {
                Text("PERFORMANCE & GRAPHICS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraCardSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Target 60 FPS Mode", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Smooth animations and 3D rendering", color = AuraMutedSilver, fontSize = 11.sp)
                        }
                        Switch(
                            checked = highFpsMode,
                            onCheckedChange = { highFpsMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AuraPurplePrimary)
                        )
                    }
                }
            }

            // Safety Section
            item {
                Text("SAFETY & COMMUNITY", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraCardSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Safe Chat Filtering", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Automatic filtering of inappropriate words", color = AuraMutedSilver, fontSize = 11.sp)
                        }
                        Switch(
                            checked = safeChatEnabled,
                            onCheckedChange = { safeChatEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AuraEmerald)
                        )
                    }
                }
            }

            // Platform Info
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("AURAVERSE", color = AuraSilver, fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 2.sp)
                    Text("v1.0.0 (Release) • Powered by Jetpack Compose 3D Engine", color = AuraMutedSilver, fontSize = 11.sp)
                }
            }
        }
    }
}
