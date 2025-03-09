package com.project.armbreaker.modules.multiplayer.data

data class GameSession (
    val sessionId: String? = null,
    val creatorId: String? = null,
    val creatorName: String? = null,
    val opponentId: String? = null,
    val opponentName: String? = null,
    val winnerId: String? = null,
    val status: String? = "waiting", // waiting, pending, ongoing, completed
)

data class OpenGame(
    val creatorName: String ="",
    val sessionId: String = "",
)