package com.project.armbreaker.modules.multiplayer.data

data class GameSession (
    val sessionId: String? = null,
    val creatorId: String? = null,
    val creatorName: String? = null,
    val opponentId: String? = null,
    val opponentName: String? = null,
    val winnerId: String? = null,
    val status: String? = null, // waiting, pending, ongoing, completed
)

data class GameList (
    val sessionId: String? = null,
    val creatorName: String? = null
)