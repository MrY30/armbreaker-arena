package com.project.armbreaker.modules.game.data

class LevelsList {
    fun loadLevels(): List<GameDataSource> {
        return listOf<GameDataSource>(
            GameDataSource(level = 1, delay = 1000, rotationAngle = 35f),
        )
    }
}