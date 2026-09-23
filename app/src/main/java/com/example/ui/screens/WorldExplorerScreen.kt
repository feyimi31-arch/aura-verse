package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CatalogData
import com.example.graphics.World3DRenderer
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun WorldExplorerScreen(
    viewModel: AuraverseViewModel
) {
    val currentDistrict by viewModel.currentDistrict.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val avatarConfig by viewModel.avatarConfig.collectAsState()
    val activeVehicle by viewModel.activeVehicle.collectAsState()
    val activePet by viewModel.activePet.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val activeNpc by viewModel.activeNpcDialogue.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    var isTurbo by remember { mutableStateOf(false) }
    var showDistrictPortal by remember { mutableStateOf(false) }
    var showEmoteWheel by remember { mutableStateOf(false) }
    var showChatOverlay by remember { mutableStateOf(false) }
    var chatInputText by remember { mutableStateOf("") }
    var cameraOrbitAngle by remember { mutableFloatStateOf(0f) }

    // Virtual Joystick coordinates
    var joystickOffset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
    val maxJoystickRadius = 55f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraDeepNavy)
    ) {
        // 1. FULL-BLEED 3D WORLD CANVAS
        World3DRenderer(
            district = currentDistrict,
            playerX = playerState.posX,
            playerY = playerState.posY,
            playerZ = playerState.posZ,
            playerRotY = playerState.rotationY,
            avatarConfig = avatarConfig,
            activeVehicle = activeVehicle,
            isMounted = playerState.isMounted,
            activePet = activePet,
            nearbyNpc = activeNpc,
            modifier = Modifier.fillMaxSize(),
            cameraOrbitAngle = cameraOrbitAngle,
            onCameraRotate = { cameraOrbitAngle = it }
        )

        // 2. TOP HUD (District Name, Fast-Travel, Currency, Menu)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // District Selector Chip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xCC0F172A),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.clickable { showDistrictPortal = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(currentDistrict.themeColor), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentDistrict.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = "Select District",
                        tint = AuraMutedSilver,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Currency & Exit to Menu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC0F172A)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Rounded.Diamond, contentDescription = "Gems", tint = AuraCyanGlow, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${profile.gems}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                IconButton(
                    onClick = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xCC0F172A), CircleShape)
                ) {
                    Icon(imageVector = Icons.Rounded.Home, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }

        // Active Emote speech bubble over player
        if (playerState.currentEmote != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-90).dp)
                    .background(AuraPurplePrimary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = playerState.currentEmote ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // 3. BOTTOM CONTROLS: VIRTUAL JOYSTICK (Left) & ACTION BUTTONS (Right)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
        ) {
            // VIRTUAL JOYSTICK
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomStart)
                    .clip(CircleShape)
                    .background(Color(0x660F172A))
                    .border(2.dp, AuraBorderGlow, CircleShape)
                    .pointerInput(isTurbo) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val newOffset = joystickOffset + dragAmount
                                val dist = sqrt(newOffset.x * newOffset.x + newOffset.y * newOffset.y)
                                joystickOffset = if (dist > maxJoystickRadius) {
                                    androidx.compose.ui.geometry.Offset(
                                        newOffset.x / dist * maxJoystickRadius,
                                        newOffset.y / dist * maxJoystickRadius
                                    )
                                } else newOffset

                                val normX = joystickOffset.x / maxJoystickRadius
                                val normY = joystickOffset.y / maxJoystickRadius
                                viewModel.movePlayer(normX, normY, isTurbo)
                            },
                            onDragEnd = {
                                joystickOffset = androidx.compose.ui.geometry.Offset.Zero
                                viewModel.movePlayer(0f, 0f, isTurbo)
                            },
                            onDragCancel = {
                                joystickOffset = androidx.compose.ui.geometry.Offset.Zero
                                viewModel.movePlayer(0f, 0f, isTurbo)
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Joystick Thumb Knob
                Box(
                    modifier = Modifier
                        .offset { IntOffset(joystickOffset.x.roundToInt(), joystickOffset.y.roundToInt()) }
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(listOf(AuraCyanSecondary, AuraPurplePrimary))
                        )
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            // ACTION BUTTONS (Right Side)
            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Chat toggle
                    IconButton(
                        onClick = { showChatOverlay = !showChatOverlay },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xCC0F172A), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Rounded.Chat, contentDescription = "Chat", tint = AuraCyanLight, modifier = Modifier.size(20.dp))
                    }

                    // Emotes Wheel
                    IconButton(
                        onClick = { showEmoteWheel = !showEmoteWheel },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xCC0F172A), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Rounded.EmojiEmotions, contentDescription = "Emotes", tint = AuraGold, modifier = Modifier.size(22.dp))
                    }

                    // Turbo Run Toggle
                    FilledIconToggleButton(
                        checked = isTurbo,
                        onCheckedChange = { isTurbo = it },
                        modifier = Modifier.size(44.dp),
                        colors = IconButtonDefaults.filledIconToggleButtonColors(
                            checkedContainerColor = AuraPinkAccent,
                            containerColor = Color(0xCC0F172A)
                        )
                    ) {
                        Icon(imageVector = Icons.Rounded.FlashOn, contentDescription = "Turbo", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Mount / Dismount Vehicle
                    if (activeVehicle != null) {
                        Button(
                            onClick = { viewModel.toggleVehicleMount() },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (playerState.isMounted) AuraPinkAccent else AuraCardSurface
                            ),
                            modifier = Modifier.size(54.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = if (playerState.isMounted) Icons.Rounded.DirectionsWalk else Icons.Rounded.TwoWheeler,
                                contentDescription = "Ride Vehicle",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    // Jump Button
                    Button(
                        onClick = { viewModel.jump() },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary),
                        modifier = Modifier
                            .size(62.dp)
                            .testTag("world_jump_button"),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = "Jump", tint = Color.White, modifier = Modifier.size(28.dp))
                            Text(text = "JUMP", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
            }
        }

        // 4. NPC DIALOGUE POPUP (When player approaches NPC)
        AnimatedVisibility(
            visible = activeNpc != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 140.dp)
                .padding(horizontal = 24.dp)
        ) {
            val npc = activeNpc
            if (npc != null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, AuraPurplePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(npc.name, fontWeight = FontWeight.Bold, color = AuraCyanSecondary, fontSize = 15.sp)
                                Text(npc.role, color = AuraMutedSilver, fontSize = 11.sp)
                            }
                            IconButton(onClick = { viewModel.closeNpcDialogue() }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close", tint = AuraMutedSilver)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(npc.greeting, color = Color.White, fontSize = 13.sp)

                        if (npc.questName != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AuraCardSurface,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("QUEST: ${npc.questName}", color = AuraGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(npc.questDesc ?: "", color = AuraSilver, fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = { viewModel.completeNpcQuest(npc) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AuraEmerald),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text("ACCEPT (+${npc.questRewardGems}💎)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. EMOTE RADIAL WHEEL DIALOG
        if (showEmoteWheel) {
            AlertDialog(
                onDismissRequest = { showEmoteWheel = false },
                containerColor = AuraDarkSurface,
                title = {
                    Text("CHOOSE EMOTE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        CatalogData.EMOTES.chunked(2).forEach { rowEmotes ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowEmotes.forEach { emote ->
                                    Button(
                                        onClick = {
                                            viewModel.playEmote(emote)
                                            showEmoteWheel = false
                                        },
                                        modifier = Modifier.weight(1f).height(46.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AuraCardSurface)
                                    ) {
                                        Text("${emote.emoji} ${emote.name}", fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }

        // 6. DISTRICT PORTAL FAST-TRAVEL SELECTOR
        if (showDistrictPortal) {
            AlertDialog(
                onDismissRequest = { showDistrictPortal = false },
                containerColor = AuraDarkSurface,
                title = {
                    Text("TRAVEL TO DISTRICT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                text = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 350.dp)
                    ) {
                        items(CatalogData.DISTRICTS) { district ->
                            val isCurrent = district.id == currentDistrict.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isCurrent) AuraPurplePrimary else AuraCardSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectDistrict(district)
                                        showDistrictPortal = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(district.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(district.tagline, color = if (isCurrent) AuraSilver else AuraMutedSilver, fontSize = 11.sp)
                                    }
                                    if (isCurrent) {
                                        Text("HERE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }

        // 7. LIVE DISTRICT CHAT OVERLAY
        AnimatedVisibility(
            visible = showChatOverlay,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 60.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF00F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBorderGlow)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("DISTRICT CHAT", color = AuraCyanSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        IconButton(onClick = { showChatOverlay = false }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close", tint = AuraMutedSilver)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(chatMessages.takeLast(6)) { msg ->
                            Row {
                                Text("${msg.sender}: ", color = Color(msg.senderColor), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(msg.text, color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatInputText,
                            onValueChange = { chatInputText = it },
                            placeholder = { Text("Say something nice...", fontSize = 11.sp, color = AuraMutedSilver) },
                            modifier = Modifier.weight(1f).height(44.dp),
                            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, color = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                viewModel.sendChatMessage(chatInputText, ChatChannel.DISTRICT)
                                chatInputText = ""
                            },
                            modifier = Modifier.size(40.dp).background(AuraPurplePrimary, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Rounded.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
