package com.example.model

data class CreatorItem(
    val id: String,
    val title: String,
    val creatorName: String,
    val category: ClothingCategory,
    val priceGems: Int,
    val patternName: String,
    val primaryColor: Long,
    val secondaryColor: Long,
    val accentColor: Long,
    val likesCount: Int = 0,
    val isOfficial: Boolean = false,
    val isOwned: Boolean = false
)
