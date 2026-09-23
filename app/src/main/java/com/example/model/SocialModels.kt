package com.example.model

data class PlayerProfile(
    val username: String = "StarStrider",
    val title: String = "Celestial Explorer",
    val bio: String = "Living in Aura City ✨ Collecting rare pets & climbing the fashion leaderboards!",
    val level: Int = 12,
    val styleScore: Int = 2480,
    val coins: Int = 4250,
    val gems: Int = 680,
    val badges: List<String> = listOf("Alpha Explorer", "Aura Idol", "Parkour Runner", "Pet Master")
)

enum class ChatChannel(val label: String) {
    DISTRICT("District"),
    GLOBAL("Global"),
    SQUAD("Squad")
}

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val channel: ChatChannel = ChatChannel.DISTRICT,
    val timestamp: String = "Now",
    val isSystem: Boolean = false,
    val senderColor: Long = 0xFF8B5CF6
)

data class Friend(
    val id: String,
    val name: String,
    val title: String,
    val isOnline: Boolean,
    val currentDistrict: DistrictId,
    val avatarLookSummary: String
)

data class EmoteAction(
    val id: String,
    val name: String,
    val emoji: String,
    val poseKey: String
)
