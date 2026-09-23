package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "StarStrider",
    val title: String = "Celestial Explorer",
    val bio: String = "Living in Aura City ✨ Collecting rare pets & climbing fashion leaderboards!",
    val level: Int = 12,
    val styleScore: Int = 2480,
    val coins: Int = 4250,
    val gems: Int = 680,
    val currentDistrict: String = "AURA_CITY",
    val activePetId: String = "pet_kitsune",
    val activeVehicleId: String = "veh_hoverboard",
    val unlockedVehiclesCsv: String = "veh_hoverboard",
    val unlockedPetsCsv: String = "pet_kitsune",
    val unlockedItemsCsv: String = "top_cyber_hoodie,bot_pleated_skirt,shoe_platform_sneakers,acc_cat_headphones,acc_angel_wings",
    val avatarConfigJson: String = ""
)
