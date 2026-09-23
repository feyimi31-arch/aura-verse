package com.example.graphics

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.model.*
import kotlin.math.*

@Composable
fun World3DRenderer(
    district: District,
    playerX: Float,
    playerY: Float,
    playerZ: Float,
    playerRotY: Float,
    avatarConfig: AvatarConfig,
    activeVehicle: Vehicle?,
    isMounted: Boolean,
    activePet: Pet?,
    nearbyNpc: NPC?,
    modifier: Modifier = Modifier,
    cameraOrbitAngle: Float = 0f,
    cameraZoom: Float = 1.0f,
    onCameraRotate: ((Float) -> Unit)? = null
) {
    var cameraAngle by remember(cameraOrbitAngle) { mutableFloatStateOf(cameraOrbitAngle) }

    val infiniteTransition = rememberInfiniteTransition(label = "world_anim")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "world_time"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    cameraAngle = (cameraAngle - dragAmount.x * 0.4f) % 360f
                    onCameraRotate?.invoke(cameraAngle)
                }
            }
    ) {
        val w = size.width
        val h = size.height

        // 1. SKYBOX GRADIENT
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(district.skyboxColorTop),
                    Color(district.skyboxColorBottom),
                    Color(district.groundColor)
                )
            ),
            topLeft = Offset.Zero,
            size = size
        )

        // Starlight / Cosmic dust in sky
        for (i in 0..35) {
            val starX = (w * ((i * 37) % 100) / 100f + sin(time + i) * 3f) % w
            val starY = (h * 0.45f * ((i * 61) % 100) / 100f)
            val starAlpha = (sin(time * 2f + i) * 0.35f + 0.65f).coerceIn(0.2f, 1f)
            drawCircle(
                color = Color.White.copy(alpha = starAlpha),
                center = Offset(starX, starY),
                radius = if (i % 4 == 0) 2.2f else 1.2f
            )
        }

        // Horizon line
        val horizonY = h * 0.42f
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(Color.Transparent, Color(district.themeColor).copy(alpha = 0.5f), Color.Transparent)
            ),
            start = Offset(0f, horizonY),
            end = Offset(w, horizonY),
            strokeWidth = 2f
        )

        // 3D Camera Projection Helper
        val camRad = Math.toRadians(cameraAngle.toDouble())
        val cosCam = cos(camRad).toFloat()
        val sinCam = sin(camRad).toFloat()

        fun project(worldX: Float, worldY: Float, worldZ: Float): Offset? {
            // Relativize to player position
            val rx = (worldX - playerX)
            val rz = (worldZ - playerZ)

            // Rotate around camera
            val cx = rx * cosCam - rz * sinCam
            val cz = rx * sinCam + rz * cosCam + 130f / cameraZoom

            if (cz <= 5f) return null // behind camera clip

            val fov = 380f * cameraZoom
            val screenX = w / 2f + (cx / cz) * fov
            val screenY = horizonY + 80f + ((worldY - playerY + 30f) / cz) * fov

            return Offset(screenX, screenY)
        }

        // 2. GROUND PLANE GRID (Perspective Cyber Lines)
        val groundColor = Color(district.groundColor)
        val gridLineColor = Color(district.themeColor).copy(alpha = 0.22f)

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(district.skyboxColorBottom).copy(alpha = 0.4f), groundColor)
            ),
            topLeft = Offset(0f, horizonY),
            size = Size(w, h - horizonY)
        )

        // Draw District-Specific Scenery & Landmarks in 3D
        when (district.id) {
            DistrictId.AURA_CITY -> drawAuraCityLandmarks(::project, time, district.themeColor)
            DistrictId.AURA_ACADEMY -> drawAcademyLandmarks(::project, time, district.themeColor)
            DistrictId.AURA_BEACH -> drawBeachLandmarks(::project, time, w, h)
            DistrictId.AURA_AMUSEMENT -> drawAmusementFerrisWheel(::project, time, district.themeColor)
            DistrictId.MYSTIC_FOREST -> drawMysticForestLandmarks(::project, time)
            DistrictId.FANTASY_REALM -> drawFantasyFloatingIsles(::project, time)
        }

        // Draw Interactive NPCs
        val npcsInDistrict = com.example.data.CatalogData.NPCS.filter { it.districtId == district.id }
        for (npc in npcsInDistrict) {
            val npcScreen = project(npc.posX, 0f, npc.posZ)
            if (npcScreen != null) {
                drawNpcEntity(npcScreen, npc, time)
            }
        }

        // 3. DRAW COMPANION PET (if active)
        if (activePet != null) {
            val petFollowRad = Math.toRadians((playerRotY + 140f).toDouble())
            val petX = playerX + (sin(petFollowRad) * 22f).toFloat()
            val petZ = playerZ + (cos(petFollowRad) * 22f).toFloat()
            val petScreen = project(petX, 0f, petZ)
            if (petScreen != null) {
                drawPetEntity(petScreen, activePet, time)
            }
        }

        // 4. DRAW PLAYER AVATAR AT CENTER WORLD POSITION
        val playerScreen = project(playerX, playerY, playerZ)
        if (playerScreen != null) {
            drawWorldPlayerAvatar(
                pos = playerScreen,
                avatarConfig = avatarConfig,
                isMounted = isMounted,
                activeVehicle = activeVehicle,
                time = time,
                playerRotY = playerRotY,
                cameraAngle = cameraAngle,
                isJumping = playerY > 0.5f
            )
        }

        // 5. INTERACTION PROMPT if near NPC
        if (nearbyNpc != null) {
            val promptBoxW = 280f
            val promptBoxH = 50f
            val promptTop = h * 0.18f
            drawRoundRect(
                color = Color(0xDD0F172A),
                topLeft = Offset(w / 2f - promptBoxW / 2f, promptTop),
                size = Size(promptBoxW, promptBoxH),
                cornerRadius = CornerRadius(25f)
            )
            drawRoundRect(
                color = Color(district.themeColor),
                topLeft = Offset(w / 2f - promptBoxW / 2f, promptTop),
                size = Size(promptBoxW, promptBoxH),
                cornerRadius = CornerRadius(25f),
                style = Stroke(2f)
            )
            drawCircle(
                color = Color(0xFFFBBF24),
                center = Offset(w / 2f - promptBoxW / 2f + 25f, promptTop + 25f),
                radius = 12f
            )
        }
    }
}

// ----------------------------------------------------
// DISTRICT LANDMARKS
// ----------------------------------------------------
private fun DrawScope.drawAuraCityLandmarks(
    project: (Float, Float, Float) -> Offset?,
    time: Float,
    themeColor: Long
) {
    // City Neon Skyscrapers
    val buildings = listOf(
        Triple(-70f, 60f, -120f),
        Triple(-30f, 90f, -140f),
        Triple(35f, 110f, -150f),
        Triple(75f, 75f, -130f),
        Triple(0f, 85f, -160f)
    )

    for ((bx, bh, bz) in buildings) {
        val baseP = project(bx, 0f, bz)
        val topP = project(bx, -bh, bz)
        if (baseP != null && topP != null) {
            val bWidth = 42f * (130f / (bz + 130f).coerceAtLeast(30f))
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                ),
                topLeft = Offset(topP.x - bWidth / 2f, topP.y),
                size = Size(bWidth, baseP.y - topP.y)
            )
            // Glowing neon spire
            drawLine(
                color = Color(themeColor),
                start = Offset(topP.x, topP.y),
                end = Offset(topP.x, topP.y - 18f),
                strokeWidth = 2.5f
            )
        }
    }

    // Central City Fountain
    val fountainBase = project(0f, 0f, 0f)
    if (fountainBase != null) {
        drawOval(
            brush = Brush.radialGradient(listOf(Color(0xFF38BDF8), Color(0x668B5CF6))),
            topLeft = Offset(fountainBase.x - 30f, fountainBase.y - 12f),
            size = Size(60f, 24f)
        )
        // Fountain water jet
        val jetTop = project(0f, -25f, 0f)
        if (jetTop != null) {
            drawLine(
                brush = Brush.verticalGradient(listOf(Color(0xCC38BDF8), Color.White)),
                start = Offset(fountainBase.x, fountainBase.y),
                end = Offset(jetTop.x, jetTop.y),
                strokeWidth = 3f
            )
        }
    }
}

private fun DrawScope.drawAcademyLandmarks(
    project: (Float, Float, Float) -> Offset?,
    time: Float,
    themeColor: Long
) {
    // Grand Clocktower Gate
    val towerBase = project(0f, 0f, -80f)
    val towerTop = project(0f, -120f, -80f)
    if (towerBase != null && towerTop != null) {
        val tw = 55f
        drawRect(
            color = Color(0xFF1E293B),
            topLeft = Offset(towerTop.x - tw / 2f, towerTop.y),
            size = Size(tw, towerBase.y - towerTop.y)
        )
        // Clock face
        drawCircle(
            color = Color(0xFFFBBF24),
            center = Offset(towerTop.x, towerTop.y + 35f),
            radius = 14f
        )
    }

    // Basketball Hoop
    val hoopBase = project(40f, 0f, -10f)
    val hoopRing = project(40f, -32f, -10f)
    if (hoopBase != null && hoopRing != null) {
        drawLine(
            color = Color(0xFF94A3B8),
            start = hoopBase,
            end = hoopRing,
            strokeWidth = 3f
        )
        // Backboard & Orange Ring
        drawRect(
            color = Color.White,
            topLeft = Offset(hoopRing.x - 14f, hoopRing.y - 16f),
            size = Size(28f, 20f)
        )
        drawOval(
            color = Color(0xFFEA580C),
            topLeft = Offset(hoopRing.x - 8f, hoopRing.y),
            size = Size(16f, 8f),
            style = Stroke(2.5f)
        )
    }
}

private fun DrawScope.drawBeachLandmarks(
    project: (Float, Float, Float) -> Offset?,
    time: Float,
    w: Float,
    h: Float
) {
    // Ocean waves shimmering
    for (i in 0..4) {
        val waveBase = project(0f, 0f, 40f + i * 25f)
        if (waveBase != null) {
            val waveWave = sin(time * 3f + i) * 6f
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, Color(0xAA38BDF8), Color.White.copy(alpha = 0.8f), Color(0xAA38BDF8), Color.Transparent)
                ),
                start = Offset(0f, waveBase.y + waveWave),
                end = Offset(w, waveBase.y + waveWave),
                strokeWidth = 4f
            )
        }
    }

    // Beach Umbrella
    val umbBase = project(-35f, 0f, 15f)
    val umbTop = project(-35f, -38f, 15f)
    if (umbBase != null && umbTop != null) {
        drawLine(color = Color(0xFFE2E8F0), start = umbBase, end = umbTop, strokeWidth = 2.5f)
        // Umbrella canopy
        val canopy = Path().apply {
            moveTo(umbTop.x - 30f, umbTop.y + 10f)
            quadraticTo(umbTop.x, umbTop.y - 14f, umbTop.x + 30f, umbTop.y + 10f)
            close()
        }
        drawPath(canopy, brush = Brush.horizontalGradient(listOf(Color(0xFFEC4899), Color(0xFF06B6D4))))
    }
}

private fun DrawScope.drawAmusementFerrisWheel(
    project: (Float, Float, Float) -> Offset?,
    time: Float,
    themeColor: Long
) {
    val centerP = project(0f, -80f, -100f)
    val baseP = project(0f, 0f, -100f)
    if (centerP != null && baseP != null) {
        val wheelRadius = 55f
        // Ferris Wheel support legs
        drawLine(color = Color(0xFF64748B), start = baseP, end = centerP, strokeWidth = 4f)
        // Outer wheel rim
        drawCircle(color = Color(themeColor), center = centerP, radius = wheelRadius, style = Stroke(3.5f))
        // Rotating spokes & cabins
        val cabinsCount = 8
        for (i in 0 until cabinsCount) {
            val ang = time * 0.8f + (i * PI.toFloat() * 2f / cabinsCount)
            val spokeX = centerP.x + cos(ang) * wheelRadius
            val spokeY = centerP.y + sin(ang) * wheelRadius
            drawLine(color = Color(0xFF94A3B8), start = centerP, end = Offset(spokeX, spokeY), strokeWidth = 1.5f)
            // Cabin
            drawRoundRect(
                color = if (i % 2 == 0) Color(0xFFEC4899) else Color(0xFF06B6D4),
                topLeft = Offset(spokeX - 6f, spokeY - 4f),
                size = Size(12f, 10f),
                cornerRadius = CornerRadius(2f)
            )
        }
    }
}

private fun DrawScope.drawMysticForestLandmarks(
    project: (Float, Float, Float) -> Offset?,
    time: Float
) {
    val mushrooms = listOf(
        Pair(-40f, -20f),
        Pair(45f, 10f),
        Pair(-25f, 35f),
        Pair(20f, -50f)
    )
    for ((mx, mz) in mushrooms) {
        val mBase = project(mx, 0f, mz)
        val mTop = project(mx, -35f, mz)
        if (mBase != null && mTop != null) {
            drawLine(color = Color(0xFF059669), start = mBase, end = mTop, strokeWidth = 4f)
            // Bioluminescent cap
            drawOval(
                brush = Brush.radialGradient(listOf(Color(0xFF34D399), Color(0xFF047857))),
                topLeft = Offset(mTop.x - 22f, mTop.y - 12f),
                size = Size(44f, 22f)
            )
        }
    }
}

private fun DrawScope.drawFantasyFloatingIsles(
    project: (Float, Float, Float) -> Offset?,
    time: Float
) {
    val isles = listOf(
        Triple(-50f, -40f, -90f),
        Triple(50f, -60f, -110f),
        Triple(0f, -80f, -130f)
    )
    for ((ix, iy, iz) in isles) {
        val isP = project(ix, iy, iz)
        if (isP != null) {
            val floatBob = sin(time * 2f + ix) * 5f
            // Floating crystal island
            val islePath = Path().apply {
                moveTo(isP.x - 45f, isP.y + floatBob)
                lineTo(isP.x + 45f, isP.y + floatBob)
                lineTo(isP.x, isP.y + floatBob + 40f)
                close()
            }
            drawPath(islePath, brush = Brush.verticalGradient(listOf(Color(0xFF7C3AED), Color(0xFF2E1065))))
            // Crystal spire on top
            drawLine(color = Color(0xFFC084FC), start = Offset(isP.x, isP.y + floatBob), end = Offset(isP.x, isP.y + floatBob - 28f), strokeWidth = 3f)
        }
    }
}

// ----------------------------------------------------
// NPC & PET ENTITIES IN WORLD
// ----------------------------------------------------
private fun DrawScope.drawNpcEntity(pos: Offset, npc: NPC, time: Float) {
    // NPC anime silhouette
    drawCircle(color = Color(0xFF8B5CF6), center = Offset(pos.x, pos.y - 45f), radius = 10f)
    drawLine(color = Color(0xFF1E1B4B), start = Offset(pos.x, pos.y - 35f), end = Offset(pos.x, pos.y - 15f), strokeWidth = 7f)
    drawLine(color = Color(0xFF0F172A), start = Offset(pos.x, pos.y - 15f), end = Offset(pos.x - 4f, pos.y), strokeWidth = 4f)
    drawLine(color = Color(0xFF0F172A), start = Offset(pos.x, pos.y - 15f), end = Offset(pos.x + 4f, pos.y), strokeWidth = 4f)

    // NPC Nameplate
    val nameW = 100f
    drawRoundRect(
        color = Color(0xCC0F172A),
        topLeft = Offset(pos.x - nameW / 2f, pos.y - 70f),
        size = Size(nameW, 18f),
        cornerRadius = CornerRadius(9f)
    )

    // Quest exclamation indicator if available
    if (npc.questName != null) {
        val questY = pos.y - 82f + sin(time * 4f) * 3f
        drawCircle(color = Color(0xFFFBBF24), center = Offset(pos.x, questY), radius = 7f)
    }
}

private fun DrawScope.drawPetEntity(pos: Offset, pet: Pet, time: Float) {
    val petColor = Color(pet.primaryColor)
    val accentColor = Color(pet.accentColor)

    val wag = sin(time * 8f) * 4f

    // Pet Body
    drawOval(
        color = petColor,
        topLeft = Offset(pos.x - 12f, pos.y - 16f),
        size = Size(24f, 16f)
    )
    // Head & Ears
    drawCircle(color = petColor, center = Offset(pos.x + 8f, pos.y - 16f), radius = 8f)
    drawLine(color = accentColor, start = Offset(pos.x + 8f, pos.y - 23f), end = Offset(pos.x + 6f, pos.y - 30f), strokeWidth = 2.5f)
    drawLine(color = accentColor, start = Offset(pos.x + 11f, pos.y - 23f), end = Offset(pos.x + 13f, pos.y - 30f), strokeWidth = 2.5f)

    // Wagging tail
    drawLine(color = accentColor, start = Offset(pos.x - 12f, pos.y - 12f), end = Offset(pos.x - 20f + wag, pos.y - 20f), strokeWidth = 3f)

    // Sparkle trail
    drawCircle(color = Color.White.copy(alpha = 0.8f), center = Offset(pos.x - 18f, pos.y - 8f), radius = 1.8f)
}

// ----------------------------------------------------
// PLAYER AVATAR IN 3D WORLD
// ----------------------------------------------------
private fun DrawScope.drawWorldPlayerAvatar(
    pos: Offset,
    avatarConfig: AvatarConfig,
    isMounted: Boolean,
    activeVehicle: Vehicle?,
    time: Float,
    playerRotY: Float,
    cameraAngle: Float,
    isJumping: Boolean
) {
    val scale = 0.65f

    // Vehicle rendering (if mounted)
    if (isMounted && activeVehicle != null) {
        val vehColor = Color(activeVehicle.neonColor)
        when (activeVehicle.type) {
            VehicleType.HOVERBOARD -> {
                // Neon Hoverboard
                drawRoundRect(
                    brush = Brush.horizontalGradient(listOf(vehColor, Color.White, vehColor)),
                    topLeft = Offset(pos.x - 26f, pos.y - 4f),
                    size = Size(52f, 8f),
                    cornerRadius = CornerRadius(4f)
                )
                // Hover glow thrusters
                drawCircle(color = vehColor.copy(alpha = 0.6f), center = Offset(pos.x - 16f, pos.y + 6f), radius = 6f)
                drawCircle(color = vehColor.copy(alpha = 0.6f), center = Offset(pos.x + 16f, pos.y + 6f), radius = 6f)
            }
            VehicleType.SPORT_BIKE, VehicleType.SCOOTER, VehicleType.CRUISER -> {
                drawRoundRect(
                    color = Color(0xFF1E1B4B),
                    topLeft = Offset(pos.x - 30f, pos.y - 14f),
                    size = Size(60f, 16f),
                    cornerRadius = CornerRadius(6f)
                )
                drawCircle(color = vehColor, center = Offset(pos.x - 22f, pos.y - 2f), radius = 8f)
                drawCircle(color = vehColor, center = Offset(pos.x + 22f, pos.y - 2f), radius = 8f)
            }
        }
    }

    // Shadow on ground
    drawOval(
        color = Color(0x44000000),
        topLeft = Offset(pos.x - 22f, pos.y - 6f),
        size = Size(44f, 12f)
    )

    // Player Anime Avatar
    val charY = if (isJumping) pos.y - 25f else if (isMounted) pos.y - 10f else pos.y

    val skinColor = Color(avatarConfig.face.skinTone)
    val hairColor = Color(avatarConfig.hair.primaryColor)
    val topColor = Color(avatarConfig.top.primaryColor)
    val botColor = Color(avatarConfig.bottom.primaryColor)

    // Legs
    drawLine(color = botColor, start = Offset(pos.x - 6f, charY - 28f), end = Offset(pos.x - 7f, charY), strokeWidth = 5.5f)
    drawLine(color = botColor, start = Offset(pos.x + 6f, charY - 28f), end = Offset(pos.x + 7f, charY), strokeWidth = 5.5f)

    // Torso & Arms
    drawLine(color = topColor, start = Offset(pos.x, charY - 55f), end = Offset(pos.x, charY - 28f), strokeWidth = 14f, cap = StrokeCap.Round)
    drawLine(color = topColor, start = Offset(pos.x - 10f, charY - 52f), end = Offset(pos.x - 14f, charY - 32f), strokeWidth = 4f)
    drawLine(color = topColor, start = Offset(pos.x + 10f, charY - 52f), end = Offset(pos.x + 14f, charY - 32f), strokeWidth = 4f)

    // Head
    drawCircle(color = skinColor, center = Offset(pos.x, charY - 65f), radius = 12f)

    // Hair
    drawCircle(color = hairColor, center = Offset(pos.x, charY - 68f), radius = 13.5f)
    // Twin tails if applicable
    if (avatarConfig.hair.category == HairCategory.TWIN) {
        val tailSway = sin(time * 6f) * 3f
        drawOval(color = hairColor, topLeft = Offset(pos.x - 24f + tailSway, charY - 72f), size = Size(9f, 26f))
        drawOval(color = hairColor, topLeft = Offset(pos.x + 15f - tailSway, charY - 72f), size = Size(9f, 26f))
    }

    // Angel wings if equipped
    if (avatarConfig.equippedAccessories.any { it.slot == AccessorySlot.BACK }) {
        drawCircle(color = Color(0xAA38BDF8), center = Offset(pos.x - 18f, charY - 52f), radius = 10f)
        drawCircle(color = Color(0xAA38BDF8), center = Offset(pos.x + 18f, charY - 52f), radius = 10f)
    }

    // Name tag above head
    val tagW = 75f
    drawRoundRect(
        color = Color(0xBB0F172A),
        topLeft = Offset(pos.x - tagW / 2f, charY - 95f),
        size = Size(tagW, 16f),
        cornerRadius = CornerRadius(8f)
    )
}
