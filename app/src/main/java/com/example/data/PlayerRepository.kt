package com.example.data

import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val dao: AuraverseDao) {
    val playerProfile: Flow<PlayerEntity?> = dao.getPlayerProfile()

    suspend fun savePlayer(entity: PlayerEntity) {
        dao.savePlayerProfile(entity)
    }

    suspend fun updateCurrency(coins: Int, gems: Int) {
        dao.updateCurrency(coins, gems)
    }
}
