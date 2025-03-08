package com.project.armbreaker.modules.screen.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LeaderboardViewModel : ViewModel() {
    var users by mutableStateOf<List<LeaderboardUser>>(emptyList())
        private set

    init {
        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            users = getLeaderboardData()
        }
    }

    private suspend fun getLeaderboardData(): List<LeaderboardUser> {
        val db = FirebaseFirestore.getInstance()
        return try {
            val snapshot = db.collection("Users")
                .orderBy("level", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.map { doc ->
                LeaderboardUser(
                    username = doc.getString("username") ?: "Unknown",
                    level = doc.getLong("level")?.toInt() ?: 0
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}