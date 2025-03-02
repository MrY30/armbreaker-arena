package com.project.armbreaker.modules.game.data

class LevelsList {
    fun loadLevels(): List<GameDataSource> {
        return listOf<GameDataSource>(
            //LEVELS 1-10
            GameDataSource(level = 1, delay = 500, enemy = 1.9f),
            GameDataSource(level = 2, delay = 500, enemy = 1f),
            GameDataSource(level = 3, delay = 500, enemy = 1f),
            GameDataSource(level = 4, delay = 500, enemy = 1f),
            GameDataSource(level = 5, delay = 500, enemy = 1f),
            GameDataSource(level = 6, delay = 500, enemy = 1f),
            GameDataSource(level = 7, delay = 500, enemy = 1f),
            GameDataSource(level = 8, delay = 500, enemy = 1f),
            GameDataSource(level = 9, delay = 500, enemy = 1f),
            GameDataSource(level = 10, delay = 500, enemy = 1.9f),

            //LEVELS 11-20
            GameDataSource(level = 11, delay = 1000, enemy = 2f),
            GameDataSource(level = 12, delay = 900, enemy = 2f),
            GameDataSource(level = 13, delay = 800, enemy = 2f),
            GameDataSource(level = 14, delay = 700, enemy = 2f),
            GameDataSource(level = 15, delay = 600, enemy = 2f),
            GameDataSource(level = 16, delay = 500, enemy = 2f),
            GameDataSource(level = 17, delay = 400, enemy = 2f),
            GameDataSource(level = 18, delay = 300, enemy = 2f),
            GameDataSource(level = 19, delay = 200, enemy = 2f),
            GameDataSource(level = 20, delay = 100, enemy = 2f),

            //LEVELS 21-30

            //LEVELS 31-40

        )
    }
}