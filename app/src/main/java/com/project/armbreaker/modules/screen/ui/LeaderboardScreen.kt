package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.armbreaker.modules.screen.data.LeaderboardUser
import com.project.armbreaker.modules.screen.data.LeaderboardViewModel

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Leaderboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        var previousLevel: Int? = null
        var rank = 0
        viewModel.users.forEachIndexed { index, user ->
            if (user.level != previousLevel) {
                rank = index + 1
            }
            previousLevel = user.level
            LeaderboardItem(rank = rank, user = user)
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, user: LeaderboardUser) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "$rank.", modifier = Modifier.weight(0.2f))
        Text(text = user.username, modifier = Modifier.weight(0.4f))
        Text(text = "Level: ${user.level}", modifier = Modifier.weight(0.4f))
    }
}
