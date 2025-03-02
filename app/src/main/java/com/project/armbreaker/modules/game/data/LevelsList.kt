package com.project.armbreaker.modules.game.data

class LevelsList {
    fun loadLevels(): List<GameDataSource> {
        return listOf<GameDataSource>(
            //LEVELS 1-10
            GameDataSource(level = 1, delay = 700, enemy = 1f),
            GameDataSource(level = 2, delay = 700, enemy = 1.5f),
            GameDataSource(level = 3, delay = 700, enemy = 2f),
            GameDataSource(level = 4, delay = 700, enemy = 2.5f),
            GameDataSource(level = 5, delay = 700, enemy = 3f),
            GameDataSource(level = 6, delay = 700, enemy = 3.5f),
            GameDataSource(level = 7, delay = 700, enemy = 4f),
            GameDataSource(level = 8, delay = 700, enemy = 4.5f),
            GameDataSource(level = 9, delay = 700, enemy = 5f),
            GameDataSource(level = 10, delay = 700, enemy = 5.5f),

            //LEVELS 11-20
            GameDataSource(level = 11, delay = 600, enemy = 1f),
            GameDataSource(level = 12, delay = 600, enemy = 1.5f),
            GameDataSource(level = 13, delay = 600, enemy = 2f),
            GameDataSource(level = 14, delay = 600, enemy = 2.5f),
            GameDataSource(level = 15, delay = 600, enemy = 3f),
            GameDataSource(level = 16, delay = 600, enemy = 3.5f),
            GameDataSource(level = 17, delay = 600, enemy = 4f),
            GameDataSource(level = 18, delay = 600, enemy = 4.5f),
            GameDataSource(level = 19, delay = 600, enemy = 5f),
            GameDataSource(level = 20, delay = 600, enemy = 5.5f),

            //LEVELS 21-30
            GameDataSource(level = 21, delay = 500, enemy = 1f),
            GameDataSource(level = 22, delay = 500, enemy = 1.5f),
            GameDataSource(level = 23, delay = 500, enemy = 2f),
            GameDataSource(level = 24, delay = 500, enemy = 2.5f),
            GameDataSource(level = 25, delay = 500, enemy = 3f),
            GameDataSource(level = 26, delay = 500, enemy = 3.5f),
            GameDataSource(level = 27, delay = 500, enemy = 4f),
            GameDataSource(level = 28, delay = 500, enemy = 4.5f),
            GameDataSource(level = 29, delay = 500, enemy = 5f),
            GameDataSource(level = 30, delay = 500, enemy = 5.5f),

            //LEVELS 31-40
            GameDataSource(level = 31, delay = 400, enemy = 1f),
            GameDataSource(level = 32, delay = 400, enemy = 1.5f),
            GameDataSource(level = 33, delay = 400, enemy = 2f),
            GameDataSource(level = 34, delay = 400, enemy = 2.5f),
            GameDataSource(level = 35, delay = 400, enemy = 3f),
            GameDataSource(level = 36, delay = 400, enemy = 3.5f),
            GameDataSource(level = 37, delay = 400, enemy = 4f),
            GameDataSource(level = 38, delay = 400, enemy = 4.5f),
            GameDataSource(level = 39, delay = 400, enemy = 5f),
            GameDataSource(level = 40, delay = 400, enemy = 5.5f),

            //LEVELS 41-50
            GameDataSource(level = 41, delay = 300, enemy = 1f),
            GameDataSource(level = 42, delay = 300, enemy = 1.5f),
            GameDataSource(level = 43, delay = 300, enemy = 2f),
            GameDataSource(level = 44, delay = 300, enemy = 2.5f),
            GameDataSource(level = 45, delay = 300, enemy = 3f),
            GameDataSource(level = 46, delay = 300, enemy = 3.5f),
            GameDataSource(level = 47, delay = 300, enemy = 4f),
            GameDataSource(level = 48, delay = 300, enemy = 4.5f),
            GameDataSource(level = 49, delay = 300, enemy = 5f),
            GameDataSource(level = 50, delay = 300, enemy = 5.5f),
        )
    }
}