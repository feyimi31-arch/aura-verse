package com.example.graphics

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import com.example.model.*
import kotlin.math.*

/**
 * 3D anime character renderer with realistic humanoid proportions and anime aesthetic.
 * Renders smooth body anatomy, expressive anime eyes, layered hairstyles, clothing, and accessories.
 */
@Composable
fun Avatar3DRenderer(
    avatarConfig: AvatarConfig,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    animationTime: Float = 0f,
    onRotationChange: ((Float) -> Unit)? = null
) {
    var rotationY by remember(avatarConfig.rotationY) { mutableFloatStateOf(avatarConfig.rotationY) }

    // Blinking timer for expressive anime eyes
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_anim")
    val blinkAnim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                1f at 0
                1f at 3700
                0.05f at 3800 // Closed eye blink
                1f at 3900
                1f at 4000
            }
        ),
        label = "blink"
    )

    // Gentle idle sway
    val swayAnim by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (isInteractive) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            rotationY = (rotationY + dragAmount.x * 0.7f) % 360f
                            onRotationChange?.invoke(rotationY)
                        }
                    }
                } else Modifier
            )
    ) {
        val centerX = size.width / 2f
        val centerY = size.height * 0.58f
        val baseScale = size.height / 520f * avatarConfig.proportions.height

        val rad = Math.toRadians(rotationY.toDouble())
        val cosRot = cos(rad).toFloat()
        val sinRot = sin(rad).toFloat()

        // Background pedestal / shadow
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x558B5CF6), Color(0x1106B6D4), Color.Transparent),
                center = Offset(centerX, centerY + 210f * baseScale),
                radius = 160f * baseScale
            ),
            topLeft = Offset(centerX - 130f * baseScale, centerY + 185f * baseScale),
            size = Size(260f * baseScale, 55f * baseScale)
        )

        // Draw back accessories (e.g. wings, backpacks) when facing front
        if (cosRot > -0.2f) {
            drawBackAccessories(avatarConfig, centerX, centerY, baseScale, cosRot, sinRot, swayAnim)
        }

        // Draw back hair layer
        drawBackHair(avatarConfig, centerX, centerY, baseScale, cosRot, sinRot, swayAnim)

        // Draw Body & Clothing
        drawHumanoidBody(
            avatarConfig = avatarConfig,
            centerX = centerX,
            centerY = centerY,
            baseScale = baseScale,
            cosRot = cosRot,
            sinRot = sinRot,
            swayAnim = swayAnim,
            blinkRatio = blinkAnim,
            animTime = animationTime
        )

        // Draw front accessories
        drawFrontAccessories(avatarConfig, centerX, centerY, baseScale, cosRot, sinRot, swayAnim)

        // Draw back accessories if facing backwards
        if (cosRot <= -0.2f) {
            drawBackAccessories(avatarConfig, centerX, centerY, baseScale, cosRot, sinRot, swayAnim)
        }

        // Special Aura Particles (if equipped)
        if (avatarConfig.equippedAccessories.any { it.slot == AccessorySlot.SPECIAL }) {
            drawFloatingAuraOrbs(centerX, centerY, baseScale, animationTime + swayAnim)
        }
    }
}

private fun DrawScope.drawBackAccessories(
    config: AvatarConfig,
    cx: Float,
    cy: Float,
    scale: Float,
    cosRot: Float,
    sinRot: Float,
    sway: Float
) {
    val wings = config.equippedAccessories.firstOrNull { it.id == "acc_angel_wings" }
    if (wings != null) {
        val wingFlap = sin(sway * 2f) * 12f
        val wingY = cy - 40f * scale
        val leftX = cx - 45f * scale * cosRot
        val rightX = cx + 45f * scale * cosRot

        // Left wing
        val leftWingPath = Path().apply {
            moveTo(leftX, wingY)
            cubicTo(
                leftX - (90f + wingFlap) * scale * cosRot, wingY - 80f * scale,
                leftX - (110f + wingFlap) * scale * cosRot, wingY + 40f * scale,
                leftX, wingY + 25f * scale
            )
            close()
        }
        drawPath(
            path = leftWingPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(wings.color).copy(alpha = 0.85f), Color(wings.secondaryColor).copy(alpha = 0.5f)),
                start = Offset(leftX, wingY),
                end = Offset(leftX - 100f * scale, wingY - 60f * scale)
            )
        )

        // Right wing
        val rightWingPath = Path().apply {
            moveTo(rightX, wingY)
            cubicTo(
                rightX + (90f + wingFlap) * scale * cosRot, wingY - 80f * scale,
                rightX + (110f + wingFlap) * scale * cosRot, wingY + 40f * scale,
                rightX, wingY + 25f * scale
            )
            close()
        }
        drawPath(
            path = rightWingPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(wings.color).copy(alpha = 0.85f), Color(wings.secondaryColor).copy(alpha = 0.5f)),
                start = Offset(rightX, wingY),
                end = Offset(rightX + 100f * scale, wingY - 60f * scale)
            )
        )
    }

    // Backpack
    val backpack = config.equippedAccessories.firstOrNull { it.id == "acc_mecha_backpack" }
    if (backpack != null && cosRot < 0.2f) {
        drawRoundRect(
            color = Color(backpack.color),
            topLeft = Offset(cx - 24f * scale, cy - 65f * scale),
            size = Size(48f * scale, 55f * scale),
            cornerRadius = CornerRadius(12f * scale)
        )
        drawCircle(
            color = Color(backpack.secondaryColor),
            center = Offset(cx, cy - 38f * scale),
            radius = 10f * scale
        )
    }
}

private fun DrawScope.drawBackHair(
    config: AvatarConfig,
    cx: Float,
    cy: Float,
    scale: Float,
    cosRot: Float,
    sinRot: Float,
    sway: Float
) {
    val hair = config.hair
    val headY = cy - 145f * scale
    val hairColor = Color(hair.primaryColor)
    val ombreColor = Color(hair.secondaryColor)

    when (hair.category) {
        HairCategory.LONG, HairCategory.TWIN -> {
            // Twin tails or flowing hair
            val swayOffset = sway * 8f * scale
            // Left tail
            drawOval(
                brush = Brush.verticalGradient(listOf(hairColor, ombreColor)),
                topLeft = Offset(cx - 58f * scale - sinRot * 20f * scale + swayOffset, headY - 10f * scale),
                size = Size(26f * scale, 130f * scale)
            )
            // Right tail
            drawOval(
                brush = Brush.verticalGradient(listOf(hairColor, ombreColor)),
                topLeft = Offset(cx + 32f * scale - sinRot * 20f * scale - swayOffset, headY - 10f * scale),
                size = Size(26f * scale, 130f * scale)
            )
            // Center mane
            drawOval(
                brush = Brush.verticalGradient(listOf(hairColor, ombreColor)),
                topLeft = Offset(cx - 36f * scale, headY - 5f * scale),
                size = Size(72f * scale, 110f * scale)
            )
        }
        HairCategory.MEDIUM, HairCategory.CURLY, HairCategory.BRAIDS -> {
            drawOval(
                brush = Brush.verticalGradient(listOf(hairColor, ombreColor)),
                topLeft = Offset(cx - 40f * scale, headY - 8f * scale),
                size = Size(80f * scale, 75f * scale)
            )
        }
        else -> {
            // Short hair back base
            drawCircle(
                color = hairColor,
                center = Offset(cx, headY + 12f * scale),
                radius = 35f * scale
            )
        }
    }
}

private fun DrawScope.drawHumanoidBody(
    avatarConfig: AvatarConfig,
    centerX: Float,
    centerY: Float,
    baseScale: Float,
    cosRot: Float,
    sinRot: Float,
    swayAnim: Float,
    blinkRatio: Float,
    animTime: Float
) {
    val prop = avatarConfig.proportions
    val skinColor = Color(avatarConfig.face.skinTone)
    val skinShadow = skinColor.copy(
        red = skinColor.red * 0.88f,
        green = skinColor.green * 0.86f,
        blue = skinColor.blue * 0.86f
    )

    val pose = avatarConfig.currentPose
    val isWalking = pose == "WALK"
    val isDancing = pose == "DANCE"
    val isSitting = pose == "SIT"
    val isVictory = pose == "VICTORY"
    val isWaving = pose == "WAVE"

    // Animation cycle parameters
    val walkPhase = animTime * 6f
    val legStride = if (isWalking) sin(walkPhase) * 28f * prop.legLength else 0f
    val armSwing = if (isWalking) -sin(walkPhase) * 24f * prop.armLength else 0f
    val hipBob = if (isWalking) abs(sin(walkPhase)) * 4f * baseScale else (swayAnim * 2f * baseScale)

    val cy = centerY + (if (isSitting) 30f * baseScale else 0f) - hipBob

    // Anatomy landmarks
    val shoulderW = 38f * baseScale * prop.shoulderWidth
    val torsoW = 30f * baseScale * prop.torsoWidth * prop.bodyWidth
    val hipW = 34f * baseScale * prop.bodyWidth
    val legL = 95f * baseScale * prop.legLength
    val armL = 75f * baseScale * prop.armLength

    val neckY = cy - 110f * baseScale
    val chestY = cy - 85f * baseScale
    val waistY = cy - 45f * baseScale
    val hipY = cy - 15f * baseScale
    val kneeY = if (isSitting) hipY + 15f * baseScale else hipY + legL * 0.52f
    val ankleY = if (isSitting) hipY + 50f * baseScale else hipY + legL

    // ----------------------------------------------------
    // 1. LEGS & SHOES
    // ----------------------------------------------------
    val leftLegX = centerX - 14f * baseScale * cosRot
    val rightLegX = centerX + 14f * baseScale * cosRot

    // Left leg
    val leftLegOffsetZ = sinRot * 14f * baseScale
    val leftStrideZ = if (isWalking) legStride * baseScale else 0f
    val rightStrideZ = if (isWalking) -legStride * baseScale else 0f

    // Determine bottom / pants colors
    val bottomColor = Color(avatarConfig.bottom.primaryColor)
    val bottomAccent = Color(avatarConfig.bottom.secondaryColor)
    val shoeColor = Color(avatarConfig.shoes.primaryColor)
    val shoeAccent = Color(avatarConfig.shoes.secondaryColor)

    val isSkirt = avatarConfig.bottom.styleKey == "PLEATED_SKIRT"

    // Draw Left Leg
    val leftLegPath = Path().apply {
        moveTo(leftLegX - 9f * baseScale * prop.legWidth, hipY)
        lineTo(leftLegX - 6f * baseScale * prop.legWidth, kneeY + leftStrideZ * 0.4f)
        lineTo(leftLegX - 5f * baseScale * prop.legWidth, ankleY + leftStrideZ)
        lineTo(leftLegX + 5f * baseScale * prop.legWidth, ankleY + leftStrideZ)
        lineTo(leftLegX + 7f * baseScale * prop.legWidth, kneeY + leftStrideZ * 0.4f)
        lineTo(leftLegX + 9f * baseScale * prop.legWidth, hipY)
        close()
    }
    drawPath(leftLegPath, color = if (isSkirt) skinColor else bottomColor)

    // Left Shoe
    val leftShoeY = ankleY + leftStrideZ
    drawRoundRect(
        color = shoeColor,
        topLeft = Offset(leftLegX - 8f * baseScale * prop.footSize, leftShoeY),
        size = Size(18f * baseScale * prop.footSize, 18f * baseScale),
        cornerRadius = CornerRadius(4f * baseScale)
    )
    drawRoundRect(
        color = shoeAccent,
        topLeft = Offset(leftLegX - 8f * baseScale * prop.footSize, leftShoeY + 12f * baseScale),
        size = Size(19f * baseScale * prop.footSize, 6f * baseScale),
        cornerRadius = CornerRadius(2f * baseScale)
    )

    // Draw Right Leg
    val rightLegPath = Path().apply {
        moveTo(rightLegX - 9f * baseScale * prop.legWidth, hipY)
        lineTo(rightLegX - 7f * baseScale * prop.legWidth, kneeY + rightStrideZ * 0.4f)
        lineTo(rightLegX - 5f * baseScale * prop.legWidth, ankleY + rightStrideZ)
        lineTo(rightLegX + 5f * baseScale * prop.legWidth, ankleY + rightStrideZ)
        lineTo(rightLegX + 6f * baseScale * prop.legWidth, kneeY + rightStrideZ * 0.4f)
        lineTo(rightLegX + 9f * baseScale * prop.legWidth, hipY)
        close()
    }
    drawPath(rightLegPath, color = if (isSkirt) skinColor else bottomColor)

    // Right Shoe
    val rightShoeY = ankleY + rightStrideZ
    drawRoundRect(
        color = shoeColor,
        topLeft = Offset(rightLegX - 9f * baseScale * prop.footSize, rightShoeY),
        size = Size(18f * baseScale * prop.footSize, 18f * baseScale),
        cornerRadius = CornerRadius(4f * baseScale)
    )
    drawRoundRect(
        color = shoeAccent,
        topLeft = Offset(rightLegX - 9f * baseScale * prop.footSize, rightShoeY + 12f * baseScale),
        size = Size(19f * baseScale * prop.footSize, 6f * baseScale),
        cornerRadius = CornerRadius(2f * baseScale)
    )

    // Skirt (if pleated skirt)
    if (isSkirt) {
        val skirtPath = Path().apply {
            moveTo(centerX - hipW * 0.6f * cosRot, waistY + 10f * baseScale)
            lineTo(centerX + hipW * 0.6f * cosRot, waistY + 10f * baseScale)
            lineTo(centerX + hipW * 0.95f * cosRot, hipY + 16f * baseScale)
            lineTo(centerX - hipW * 0.95f * cosRot, hipY + 16f * baseScale)
            close()
        }
        drawPath(skirtPath, color = bottomColor)
        // Skirt pleat lines
        for (i in -3..3) {
            val pleatX1 = centerX + (i * 5f) * baseScale * cosRot
            val pleatX2 = centerX + (i * 9f) * baseScale * cosRot
            drawLine(
                color = bottomAccent.copy(alpha = 0.5f),
                start = Offset(pleatX1, waistY + 10f * baseScale),
                end = Offset(pleatX2, hipY + 16f * baseScale),
                strokeWidth = 2f * baseScale
            )
        }
    }

    // ----------------------------------------------------
    // 2. TORSO & CHEST
    // ----------------------------------------------------
    val topColor = Color(avatarConfig.top.primaryColor)
    val topSecondary = Color(avatarConfig.top.secondaryColor)
    val topAccent = Color(avatarConfig.top.accentColor)

    // Torso body contour
    val torsoPath = Path().apply {
        moveTo(centerX - shoulderW * cosRot, chestY)
        lineTo(centerX - torsoW * 0.7f * cosRot, waistY)
        lineTo(centerX - hipW * 0.6f * cosRot, waistY + 12f * baseScale)
        lineTo(centerX + hipW * 0.6f * cosRot, waistY + 12f * baseScale)
        lineTo(centerX + torsoW * 0.7f * cosRot, waistY)
        lineTo(centerX + shoulderW * cosRot, chestY)
        close()
    }
    drawPath(torsoPath, color = topColor)

    // Clothing collar / chest detail
    val collarPath = Path().apply {
        moveTo(centerX - 14f * baseScale * cosRot, chestY - 15f * baseScale)
        lineTo(centerX, chestY + 5f * baseScale)
        lineTo(centerX + 14f * baseScale * cosRot, chestY - 15f * baseScale)
    }
    drawPath(collarPath, color = topSecondary, style = Stroke(width = 3f * baseScale))

    // Crop top midriff skin (if crop hoodie)
    if (avatarConfig.top.styleKey == "CROP_HOODIE") {
        drawRect(
            color = skinColor,
            topLeft = Offset(centerX - torsoW * 0.55f * cosRot, waistY - 8f * baseScale),
            size = Size(torsoW * 1.1f * abs(cosRot), 15f * baseScale)
        )
    }

    // ----------------------------------------------------
    // 3. ARMS & HANDS
    // ----------------------------------------------------
    // Left Arm
    val leftShoulderX = centerX - (shoulderW + 4f * baseScale) * cosRot
    val leftShoulderY = chestY
    val leftArmAngle = if (isWalking) armSwing else if (isDancing) 30f + sin(animTime * 8f) * 25f else 10f
    drawArm(
        startX = leftShoulderX,
        startY = leftShoulderY,
        armLength = armL,
        armWidth = 8f * baseScale * prop.armWidth,
        handSize = 8f * baseScale * prop.handSize,
        angleDeg = leftArmAngle,
        sleeveColor = topColor,
        skinColor = skinColor,
        scale = baseScale,
        cosRot = cosRot
    )

    // Right Arm
    val rightShoulderX = centerX + (shoulderW + 4f * baseScale) * cosRot
    val rightShoulderY = chestY
    val rightArmAngle = when {
        isWaving -> -135f + sin(animTime * 10f) * 20f // waving hand high
        isVictory -> -120f // peace sign
        isDancing -> -30f - sin(animTime * 8f) * 25f
        isWalking -> -armSwing
        else -> -10f
    }
    drawArm(
        startX = rightShoulderX,
        startY = rightShoulderY,
        armLength = armL,
        armWidth = 8f * baseScale * prop.armWidth,
        handSize = 8f * baseScale * prop.handSize,
        angleDeg = rightArmAngle,
        sleeveColor = topColor,
        skinColor = skinColor,
        scale = baseScale,
        cosRot = cosRot,
        isVictoryHand = isVictory
    )

    // ----------------------------------------------------
    // 4. NECK & HEAD
    // ----------------------------------------------------
    // Neck
    drawRect(
        color = skinShadow,
        topLeft = Offset(centerX - 8f * baseScale * cosRot, neckY),
        size = Size(16f * baseScale * abs(cosRot), 22f * baseScale)
    )

    // Head base
    val headCenterY = neckY - 26f * baseScale
    val headRadiusX = 28f * baseScale
    val headRadiusY = 32f * baseScale

    // Anime jawline / chin
    val facePath = Path().apply {
        val chinY = headCenterY + headRadiusY
        val leftEar = Offset(centerX - headRadiusX * cosRot, headCenterY - 4f * baseScale)
        val rightEar = Offset(centerX + headRadiusX * cosRot, headCenterY - 4f * baseScale)
        val chin = Offset(centerX, chinY)

        moveTo(leftEar.x, leftEar.y)
        cubicTo(
            leftEar.x, chinY - 8f * baseScale,
            chin.x - 12f * baseScale * cosRot, chinY,
            chin.x, chinY
        )
        cubicTo(
            chin.x + 12f * baseScale * cosRot, chinY,
            rightEar.x, chinY - 8f * baseScale,
            rightEar.x, rightEar.y
        )
        cubicTo(
            rightEar.x, headCenterY - headRadiusY,
            leftEar.x, headCenterY - headRadiusY,
            leftEar.x, leftEar.y
        )
        close()
    }
    drawPath(facePath, color = skinColor)

    // ----------------------------------------------------
    // 5. ANIME FACE (Eyes, Nose, Mouth, Blush)
    // Only visible if facing forward
    // ----------------------------------------------------
    if (cosRot > 0.1f) {
        val face = avatarConfig.face
        val eyeSpacing = 16f * baseScale * cosRot
        val eyeY = headCenterY + 2f * baseScale
        val eyeSize = 10f * baseScale * face.eyeSize

        // Blush
        if (face.blushStyle != BlushStyle.NONE) {
            val blushColor = Color(0x55F43F5E)
            drawOval(
                color = blushColor,
                topLeft = Offset(centerX - eyeSpacing - 12f * baseScale, eyeY + 12f * baseScale),
                size = Size(14f * baseScale, 6f * baseScale)
            )
            drawOval(
                color = blushColor,
                topLeft = Offset(centerX + eyeSpacing - 2f * baseScale, eyeY + 12f * baseScale),
                size = Size(14f * baseScale, 6f * baseScale)
            )
        }

        // Left Eye
        drawAnimeEye(
            centerX = centerX - eyeSpacing,
            centerY = eyeY,
            size = eyeSize,
            irisColor = Color(face.eyeColor),
            blink = blinkRatio,
            scale = baseScale
        )

        // Right Eye
        drawAnimeEye(
            centerX = centerX + eyeSpacing,
            centerY = eyeY,
            size = eyeSize,
            irisColor = Color(face.eyeColor),
            blink = if (isVictory) 0.05f else blinkRatio, // wink on victory!
            scale = baseScale
        )

        // Eyebrows
        val browY = eyeY - eyeSize * 1.15f
        drawLine(
            color = Color(face.eyebrowColor),
            start = Offset(centerX - eyeSpacing - 9f * baseScale, browY + 1f * baseScale),
            end = Offset(centerX - eyeSpacing + 9f * baseScale, browY - 2f * baseScale),
            strokeWidth = 2.2f * baseScale,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(face.eyebrowColor),
            start = Offset(centerX + eyeSpacing - 9f * baseScale, browY - 2f * baseScale),
            end = Offset(centerX + eyeSpacing + 9f * baseScale, browY + 1f * baseScale),
            strokeWidth = 2.2f * baseScale,
            cap = StrokeCap.Round
        )

        // Nose (Cute anime shadow dot)
        drawCircle(
            color = skinShadow,
            center = Offset(centerX, eyeY + 14f * baseScale),
            radius = 1.4f * baseScale
        )

        // Mouth (Smile / Smirk)
        val mouthY = eyeY + 22f * baseScale
        val mouthPath = Path().apply {
            moveTo(centerX - 6f * baseScale * cosRot, mouthY)
            quadraticTo(
                centerX, mouthY + (if (face.mouthStyle == MouthStyle.PLAYFUL_POUT) -2f else 4f) * baseScale,
                centerX + 6f * baseScale * cosRot, mouthY
            )
        }
        drawPath(mouthPath, color = Color(0xFFE11D48), style = Stroke(width = 2.2f * baseScale, cap = StrokeCap.Round))

        // Glasses accessory
        if (avatarConfig.equippedAccessories.any { it.slot == AccessorySlot.FACE && it.id == "acc_anime_glasses" }) {
            drawAnimeGlasses(centerX, eyeY, eyeSpacing, eyeSize, baseScale)
        }
        // Cyber visor accessory
        if (avatarConfig.equippedAccessories.any { it.slot == AccessorySlot.FACE && it.id == "acc_cyber_visor" }) {
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color(0xCC06B6D4), Color(0xCCF43F5E))),
                topLeft = Offset(centerX - eyeSpacing - 12f * baseScale, eyeY - 6f * baseScale),
                size = Size((eyeSpacing * 2 + 24f * baseScale), 14f * baseScale),
                cornerRadius = CornerRadius(4f * baseScale)
            )
        }
    }

    // ----------------------------------------------------
    // 6. FRONT HAIR (Bangs, Tufts, Highlights)
    // ----------------------------------------------------
    drawFrontHair(avatarConfig, centerX, headCenterY, baseScale, cosRot, sinRot, swayAnim)
}

private fun DrawScope.drawAnimeEye(
    centerX: Float,
    centerY: Float,
    size: Float,
    irisColor: Color,
    blink: Float,
    scale: Float
) {
    if (blink < 0.15f) {
        // Closed anime eye arc with cute lash
        val closedPath = Path().apply {
            moveTo(centerX - size * 0.9f, centerY)
            quadraticTo(centerX, centerY + size * 0.4f, centerX + size * 0.9f, centerY - size * 0.2f)
        }
        drawPath(closedPath, color = Color(0xFF1E1B4B), style = Stroke(width = 2.8f * scale, cap = StrokeCap.Round))
        return
    }

    val eyeHeight = size * 1.35f * blink

    // Eye White (Sclera)
    drawOval(
        color = Color(0xFFFFFDFD),
        topLeft = Offset(centerX - size * 0.85f, centerY - eyeHeight * 0.5f),
        size = Size(size * 1.7f, eyeHeight)
    )

    // Iris Gradient
    val irisRadius = size * 0.7f
    val irisHeight = eyeHeight * 0.9f
    drawOval(
        brush = Brush.verticalGradient(
            colors = listOf(irisColor.copy(red = irisColor.red * 0.4f), irisColor, irisColor.copy(alpha = 0.9f))
        ),
        topLeft = Offset(centerX - irisRadius, centerY - irisHeight * 0.5f),
        size = Size(irisRadius * 2f, irisHeight)
    )

    // Pupil
    drawCircle(
        color = Color(0xFF0F0C20),
        center = Offset(centerX, centerY),
        radius = irisRadius * 0.4f * blink
    )

    // Anime Starlight Highlights (2 reflections: one large upper-left, one small lower-right)
    drawCircle(
        color = Color.White,
        center = Offset(centerX - irisRadius * 0.35f, centerY - irisHeight * 0.25f),
        radius = irisRadius * 0.3f * blink
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.8f),
        center = Offset(centerX + irisRadius * 0.3f, centerY + irisHeight * 0.2f),
        radius = irisRadius * 0.15f * blink
    )

    // Upper Eyelash line (stylized anime thick wing)
    val lashPath = Path().apply {
        moveTo(centerX - size * 0.9f, centerY - eyeHeight * 0.25f)
        quadraticTo(centerX, centerY - eyeHeight * 0.62f, centerX + size * 0.95f, centerY - eyeHeight * 0.45f)
    }
    drawPath(lashPath, color = Color(0xFF110E1E), style = Stroke(width = 3.2f * scale, cap = StrokeCap.Round))
}

private fun DrawScope.drawFrontHair(
    config: AvatarConfig,
    cx: Float,
    headCy: Float,
    scale: Float,
    cosRot: Float,
    sinRot: Float,
    sway: Float
) {
    val hair = config.hair
    val hairColor = Color(hair.primaryColor)
    val ombreColor = Color(hair.secondaryColor)
    val highlightColor = Color(hair.highlightColor)

    val topY = headCy - 38f * scale

    // Hair Top Volume
    val topDome = Path().apply {
        moveTo(cx - 32f * scale * cosRot, headCy - 10f * scale)
        cubicTo(
            cx - 36f * scale * cosRot, topY - 10f * scale,
            cx + 36f * scale * cosRot, topY - 10f * scale,
            cx + 32f * scale * cosRot, headCy - 10f * scale
        )
        close()
    }
    drawPath(topDome, color = hairColor)

    // Anime Bangs / Strands
    val swayX = sway * 3f * scale
    val strand1 = Path().apply {
        moveTo(cx - 24f * scale * cosRot, topY + 12f * scale)
        quadraticTo(cx - 18f * scale * cosRot + swayX, headCy - 2f * scale, cx - 14f * scale * cosRot, headCy + 4f * scale)
        quadraticTo(cx - 8f * scale * cosRot, headCy - 4f * scale, cx - 12f * scale * cosRot, topY + 12f * scale)
        close()
    }
    drawPath(strand1, color = hairColor)

    val strand2 = Path().apply {
        moveTo(cx - 8f * scale * cosRot, topY + 10f * scale)
        quadraticTo(cx + swayX, headCy + 6f * scale, cx + 4f * scale * cosRot, headCy + 9f * scale)
        quadraticTo(cx + 8f * scale * cosRot, headCy - 2f * scale, cx + 6f * scale * cosRot, topY + 10f * scale)
        close()
    }
    drawPath(strand2, color = hairColor)

    val strand3 = Path().apply {
        moveTo(cx + 10f * scale * cosRot, topY + 12f * scale)
        quadraticTo(cx + 20f * scale * cosRot + swayX, headCy - 2f * scale, cx + 22f * scale * cosRot, headCy + 5f * scale)
        quadraticTo(cx + 26f * scale * cosRot, headCy - 6f * scale, cx + 24f * scale * cosRot, topY + 12f * scale)
        close()
    }
    drawPath(strand3, color = hairColor)

    // Anime Hair Gloss / Starlight Arc
    if (hair.hasHighlights) {
        drawLine(
            color = highlightColor.copy(alpha = 0.7f),
            start = Offset(cx - 22f * scale * cosRot, topY + 4f * scale),
            end = Offset(cx + 22f * scale * cosRot, topY + 4f * scale),
            strokeWidth = 3f * scale,
            cap = StrokeCap.Round
        )
    }

    // Side Locks
    drawOval(
        color = ombreColor,
        topLeft = Offset(cx - 34f * scale * cosRot + swayX, headCy - 6f * scale),
        size = Size(10f * scale, 38f * scale)
    )
    drawOval(
        color = ombreColor,
        topLeft = Offset(cx + 24f * scale * cosRot - swayX, headCy - 6f * scale),
        size = Size(10f * scale, 38f * scale)
    )
}

private fun DrawScope.drawFrontAccessories(
    config: AvatarConfig,
    cx: Float,
    cy: Float,
    scale: Float,
    cosRot: Float,
    sinRot: Float,
    sway: Float
) {
    val headCy = cy - 171f * scale

    // Cat ear headphones
    val headphones = config.equippedAccessories.firstOrNull { it.id == "acc_cat_headphones" }
    if (headphones != null) {
        val hpColor = Color(headphones.color)
        val glowColor = Color(headphones.secondaryColor)

        // Headband arc
        val arcPath = Path().apply {
            moveTo(cx - 34f * scale * cosRot, headCy + 2f * scale)
            cubicTo(
                cx - 36f * scale * cosRot, headCy - 44f * scale,
                cx + 36f * scale * cosRot, headCy - 44f * scale,
                cx + 34f * scale * cosRot, headCy + 2f * scale
            )
        }
        drawPath(arcPath, color = hpColor, style = Stroke(width = 4.5f * scale, cap = StrokeCap.Round))

        // Left Cat Ear
        val leftEarPath = Path().apply {
            moveTo(cx - 28f * scale * cosRot, headCy - 34f * scale)
            lineTo(cx - 36f * scale * cosRot, headCy - 62f * scale)
            lineTo(cx - 16f * scale * cosRot, headCy - 40f * scale)
            close()
        }
        drawPath(leftEarPath, color = hpColor)
        drawPath(leftEarPath, color = glowColor, style = Stroke(width = 2f * scale))

        // Right Cat Ear
        val rightEarPath = Path().apply {
            moveTo(cx + 16f * scale * cosRot, headCy - 40f * scale)
            lineTo(cx + 36f * scale * cosRot, headCy - 62f * scale)
            lineTo(cx + 28f * scale * cosRot, headCy - 34f * scale)
            close()
        }
        drawPath(rightEarPath, color = hpColor)
        drawPath(rightEarPath, color = glowColor, style = Stroke(width = 2f * scale))

        // Ear cups with glow
        drawCircle(color = glowColor, center = Offset(cx - 34f * scale * cosRot, headCy + 6f * scale), radius = 8f * scale)
        drawCircle(color = glowColor, center = Offset(cx + 34f * scale * cosRot, headCy + 6f * scale), radius = 8f * scale)
    }

    // Halo
    val halo = config.equippedAccessories.firstOrNull { it.id == "acc_halo" }
    if (halo != null) {
        val haloY = headCy - 55f * scale + sway * 3f * scale
        drawOval(
            color = Color(halo.color),
            topLeft = Offset(cx - 32f * scale, haloY),
            size = Size(64f * scale, 16f * scale),
            style = Stroke(width = 3.5f * scale)
        )
    }
}

private fun DrawScope.drawArm(
    startX: Float,
    startY: Float,
    armLength: Float,
    armWidth: Float,
    handSize: Float,
    angleDeg: Float,
    sleeveColor: Color,
    skinColor: Color,
    scale: Float,
    cosRot: Float,
    isVictoryHand: Boolean = false
) {
    val rad = Math.toRadians(angleDeg.toDouble())
    val endX = startX + (sin(rad) * armLength * cosRot).toFloat()
    val endY = startY + (cos(rad) * armLength).toFloat()

    // Sleeve / Arm segment
    drawLine(
        color = sleeveColor,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = armWidth,
        cap = StrokeCap.Round
    )

    // Hand
    drawCircle(
        color = skinColor,
        center = Offset(endX, endY),
        radius = handSize
    )

    if (isVictoryHand) {
        // Peace fingers
        drawLine(
            color = skinColor,
            start = Offset(endX, endY),
            end = Offset(endX - 5f * scale, endY - 12f * scale),
            strokeWidth = 2.5f * scale,
            cap = StrokeCap.Round
        )
        drawLine(
            color = skinColor,
            start = Offset(endX, endY),
            end = Offset(endX + 5f * scale, endY - 12f * scale),
            strokeWidth = 2.5f * scale,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawAnimeGlasses(
    cx: Float,
    eyeY: Float,
    spacing: Float,
    eyeSize: Float,
    scale: Float
) {
    val glassColor = Color(0xFFCBD5E1)
    val glassRadius = eyeSize * 1.05f
    drawCircle(color = glassColor, center = Offset(cx - spacing, eyeY), radius = glassRadius, style = Stroke(1.8f * scale))
    drawCircle(color = glassColor, center = Offset(cx + spacing, eyeY), radius = glassRadius, style = Stroke(1.8f * scale))
    drawLine(color = glassColor, start = Offset(cx - spacing + glassRadius, eyeY), end = Offset(cx + spacing - glassRadius, eyeY), strokeWidth = 1.8f * scale)
}

private fun DrawScope.drawFloatingAuraOrbs(
    cx: Float,
    cy: Float,
    scale: Float,
    time: Float
) {
    for (i in 0..4) {
        val angle = time * 2f + (i * PI.toFloat() * 2f / 5f)
        val orbRadius = 75f * scale + sin(time * 3f + i) * 15f * scale
        val orbX = cx + cos(angle) * orbRadius
        val orbY = cy - 60f * scale + sin(angle * 1.5f) * 40f * scale

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF38BDF8), Color(0x668B5CF6), Color.Transparent),
                center = Offset(orbX, orbY),
                radius = 16f * scale
            ),
            center = Offset(orbX, orbY),
            radius = 16f * scale
        )
        drawCircle(
            color = Color.White,
            center = Offset(orbX, orbY),
            radius = 3f * scale
        )
    }
}
