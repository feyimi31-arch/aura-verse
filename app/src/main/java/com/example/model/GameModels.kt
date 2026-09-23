package com.example.model

enum class MiniGameType(val title: String, val description: String, val rewardGems: Int) {
    PARKOUR_OBBY("Neon Skylines Obby", "Jump across floating platforms and laser hurdles against the clock!", 25),
    DANCE_CHALLENGE("Aura Rhythm Beat", "Tap rhythm arrows to the beat of anime synthwave tracks!", 20),
    BASKETBALL_SHOOTOUT("Academy Hoop Master", "Arc shots from dynamic spots before the buzzer sounds!", 15)
}

data class DanceNote(
    val id: Int,
    val direction: Int, // 0 = Left, 1 = Down, 2 = Up, 3 = Right
    val targetTimeMs: Long,
    var isHit: Boolean = false,
    var rating: String? = null // PERFECT, GREAT, OK, MISS
)

data class ParkourPlatform(
    val id: Int,
    val x: Float,
    val y: Float,
    val z: Float,
    val width: Float,
    val depth: Float,
    val isMoving: Boolean = false,
    val color: Long = 0xFF8B5CF6
)
