package com.project.armbreaker.modules.multiplayer.data

data class GameSession (
    val sessionId: String? = null,
    val creatorId: String? = null,
    val creatorName: String? = null,
    val creatorScore: Int = 0,
    val opponentId: String? = null,
    val opponentName: String? = null,
    val opponentScore: Int = 0,
    val winnerId: String? = null,
    val status: String? = null, // waiting, pending, ongoing, completed
    val ready: Int = 0 //0 = not ready, 1 = 1 player is ready, 2 = both players are ready
)

data class GameList (
    val sessionId: String? = null,
    val creatorName: String? = null
)