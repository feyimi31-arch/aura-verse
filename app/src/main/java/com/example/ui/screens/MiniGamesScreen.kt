package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MiniGameType
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute
import kotlinx.coroutines.delay
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGamesScreen(
    viewModel: AuraverseViewModel
) {
    val activeGame by viewModel.activeMiniGame.collectAsState()
    val score by viewModel.miniGameScore.collectAsState()

    Scaffold(
        containerColor = AuraDeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (activeGame != null) activeGame!!.title else "MINI-GAMES",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (activeGame != null) viewModel.exitMiniGame() else viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                    }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AuraDarkSurface)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (activeGame == null) {
                // Mini-Games Selection Menu
                MiniGamesList(viewModel)
            } else {
                when (activeGame!!) {
                    MiniGameType.PARKOUR_OBBY -> ParkourObbyGame(viewModel, score)
                    MiniGameType.DANCE_CHALLENGE -> RhythmDanceGame(viewModel, score)
                    MiniGameType.BASKETBALL_SHOOTOUT -> BasketballShootoutGame(viewModel, score)
                }
            }
        }
    }
}

@Composable
fun MiniGamesList(viewModel: AuraverseViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "COMMUNITY CHALLENGES",
                color = AuraMutedSilver,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        items(MiniGameType.entries) { game ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBorderGlow),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.startMiniGame(game) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(game.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AuraPurpleDark
                        ) {
                            Text(
                                "+${game.rewardGems} 💎",
                                color = AuraCyanGlow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(game.description, color = AuraMutedSilver, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.startMiniGame(game) },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
                    ) {
                        Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("PLAY NOW", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 1. PARKOUR OBBY MINI-GAME
// ----------------------------------------------------
@Composable
fun ParkourObbyGame(viewModel: AuraverseViewModel, score: Int) {
    var playerPlatform by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var gameOver by remember { mutableStateOf(false) }

    val totalPlatforms = 12

    LaunchedEffect(Unit) {
        while (timeLeft > 0 && !gameOver) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft <= 0) gameOver = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Status Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Checkpoint: $playerPlatform / $totalPlatforms", color = AuraCyanLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Time: ${timeLeft}s", color = if (timeLeft < 10) AuraPinkAccent else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Score: $score", color = AuraGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        // 3D Obby Platforms Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AuraDarkSurface, RoundedCornerShape(16.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw floating neon platforms
                for (i in 0 until totalPlatforms) {
                    val platY = h - (i + 1) * (h / (totalPlatforms + 1))
                    val platX = w / 2f + sin(i * 1.2f) * (w * 0.28f)
                    val isCurrent = i == playerPlatform
                    val isPast = i < playerPlatform

                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            listOf(
                                if (isCurrent) Color(0xFF38BDF8) else if (isPast) Color(0xFF10B981) else Color(0xFF8B5CF6),
                                if (isCurrent) Color(0xFFEC4899) else Color(0xFF6D28D9)
                            )
                        ),
                        topLeft = Offset(platX - 45f, platY - 10f),
                        size = Size(90f, 20f),
                        cornerRadius = CornerRadius(6f)
                    )

                    // Draw Character on current platform
                    if (isCurrent) {
                        drawCircle(color = Color(0xFFFDE4D0), center = Offset(platX, platY - 32f), radius = 10f)
                        drawLine(color = Color(0xFF8B5CF6), start = Offset(platX, platY - 22f), end = Offset(platX, platY - 10f), strokeWidth = 8f)
                    }
                }
            }

            if (gameOver || playerPlatform >= totalPlatforms - 1) {
                Surface(
                    color = Color(0xEE0F172A),
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (playerPlatform >= totalPlatforms - 1) "🏆 OBBY COMPLETED!" else "TIME EXPIRED!",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Final Score: $score", color = AuraGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.finishMiniGame(score) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
                        ) {
                            Text("CLAIM REWARDS")
                        }
                    }
                }
            }
        }

        // Jump to next platform button
        Button(
            onClick = {
                if (playerPlatform < totalPlatforms - 1 && !gameOver) {
                    playerPlatform++
                    viewModel.incrementMiniGameScore(20)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("obby_jump_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AuraCyanSecondary)
        ) {
            Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("JUMP PLATFORM", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.Black)
        }
    }
}

// ----------------------------------------------------
// 2. RHYTHM DANCE CHALLENGE MINI-GAME
// ----------------------------------------------------
@Composable
fun RhythmDanceGame(viewModel: AuraverseViewModel, score: Int) {
    var combo by remember { mutableIntStateOf(0) }
    var ratingText by remember { mutableStateOf<String?>(null) }
    var activeTargetLane by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1200)
            activeTargetLane = (0..3).random()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Combo: ${combo}x", color = AuraPinkAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Score: $score", color = AuraGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Button(
                onClick = { viewModel.finishMiniGame(score) },
                colors = ButtonDefaults.buttonColors(containerColor = AuraCardSurface),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("FINISH", fontSize = 11.sp)
            }
        }

        // Rating popup
        Text(
            text = ratingText ?: "TAP ARROWS TO THE BEAT",
            color = if (ratingText == "PERFECT!") AuraCyanGlow else AuraWhite,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )

        // 4 Rhythm Arrows
        val arrows = listOf("LEFT" to "⬅️", "DOWN" to "⬇️", "UP" to "⬆️", "RIGHT" to "➡️")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            arrows.forEachIndexed { index, (label, emoji) ->
                val isTarget = index == activeTargetLane
                Button(
                    onClick = {
                        if (isTarget) {
                            combo++
                            ratingText = "PERFECT!"
                            viewModel.incrementMiniGameScore(15 + combo * 2)
                        } else {
                            combo = 0
                            ratingText = "MISS!"
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTarget) AuraPinkAccent else AuraCardSurface
                    )
                ) {
                    Text(emoji, fontSize = 28.sp)
                }
            }
        }
    }
}

// ----------------------------------------------------
// 3. BASKETBALL SHOOTOUT MINI-GAME
// ----------------------------------------------------
@Composable
fun BasketballShootoutGame(viewModel: AuraverseViewModel, score: Int) {
    var aimAngle by remember { mutableFloatStateOf(45f) }
    var shotsMade by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("Adjust angle & Shoot!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Baskets: $shotsMade", color = AuraGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("Score: $score", color = AuraCyanLight, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Button(
                onClick = { viewModel.finishMiniGame(score) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AuraCardSurface)
            ) {
                Text("FINISH", fontSize = 11.sp)
            }
        }

        Text(message, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        // Court Preview Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(AuraCardSurface, RoundedCornerShape(16.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Hoop on right
                drawRect(color = Color.White, topLeft = Offset(w - 60f, 40f), size = Size(8f, 50f))
                drawOval(color = Color(0xFFEA580C), topLeft = Offset(w - 90f, 65f), size = Size(35f, 12f))

                // Ball on left
                drawCircle(color = Color(0xFFF97316), center = Offset(60f, h - 50f), radius = 16f)

                // Trajectory Aim Line
                val rad = Math.toRadians(aimAngle.toDouble())
                val aimEndX = 60f + kotlin.math.cos(rad).toFloat() * 90f
                val aimEndY = (h - 50f) - kotlin.math.sin(rad).toFloat() * 90f
                drawLine(
                    color = Color(0xFF38BDF8),
                    start = Offset(60f, h - 50f),
                    end = Offset(aimEndX, aimEndY),
                    strokeWidth = 3f
                )
            }
        }

        // Aim Slider
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Aim Angle: ${aimAngle.toInt()}°", color = AuraSilver, fontSize = 13.sp)
            Slider(
                value = aimAngle,
                onValueChange = { aimAngle = it },
                valueRange = 20f..75f,
                colors = SliderDefaults.colors(thumbColor = AuraGold, activeTrackColor = AuraPurplePrimary)
            )
        }

        // Shoot Button
        Button(
            onClick = {
                // Sweet spot angle between 48 and 56 degrees
                if (aimAngle in 48f..56f) {
                    shotsMade++
                    message = "🏀 SWISH! BUCKET!"
                    viewModel.incrementMiniGameScore(30)
                } else {
                    message = "CLANG! Off the rim!"
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary)
        ) {
            Text("SHOOT BALL 🏀", fontWeight = FontWeight.Black, fontSize = 15.sp)
        }
    }
}
