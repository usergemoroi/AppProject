package com.bombaday.app.repository

import com.bombaday.app.model.Achievement
import com.bombaday.app.model.UserProfile
import com.bombaday.app.model.UserSettings
import com.bombaday.app.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection(Constants.COLLECTION_USERS)
    private val achievementsCollection = firestore.collection(Constants.COLLECTION_ACHIEVEMENTS)
    
    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            snapshot.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun createOrUpdateUser(userId: String, deviceId: String): UserProfile {
        val existingUser = getUserProfile(userId)
        
        return if (existingUser != null) {
            existingUser
        } else {
            val newUser = UserProfile(
                userId = userId,
                deviceId = deviceId,
                streak = 0,
                coins = 0,
                totalBombs = 0,
                topTenCount = 0,
                settings = UserSettings()
            )
            usersCollection.document(userId).set(newUser).await()
            newUser
        }
    }
    
    suspend fun updateStreak(userId: String): Int {
        return try {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val userRef = usersCollection.document(userId)
            
            firestore.runTransaction { transaction ->
                val user = transaction.get(userRef).toObject(UserProfile::class.java)
                
                if (user != null) {
                    val yesterday = getYesterdayDate()
                    val newStreak = when {
                        user.lastBombDate == today -> user.streak
                        user.lastBombDate == yesterday -> user.streak + 1
                        else -> 1
                    }
                    
                    transaction.update(userRef, mapOf(
                        "streak" to newStreak,
                        "lastBombDate" to today,
                        "totalBombs" to user.totalBombs + 1
                    ))
                    
                    newStreak
                } else {
                    0
                }
            }.await()
        } catch (e: Exception) {
            0
        }
    }
    
    suspend fun addCoins(userId: String, amount: Int): Int {
        return try {
            val userRef = usersCollection.document(userId)
            
            firestore.runTransaction { transaction ->
                val user = transaction.get(userRef).toObject(UserProfile::class.java)
                
                if (user != null) {
                    val newCoins = user.coins + amount
                    transaction.update(userRef, "coins", newCoins)
                    newCoins
                } else {
                    0
                }
            }.await()
        } catch (e: Exception) {
            0
        }
    }
    
    suspend fun updateTopTenCount(userId: String) {
        try {
            val userRef = usersCollection.document(userId)
            
            firestore.runTransaction { transaction ->
                val user = transaction.get(userRef).toObject(UserProfile::class.java)
                
                if (user != null) {
                    transaction.update(userRef, "topTenCount", user.topTenCount + 1)
                }
            }.await()
        } catch (e: Exception) {
            // Ignore
        }
    }
    
    suspend fun updateSettings(userId: String, settings: UserSettings) {
        try {
            usersCollection.document(userId).update("settings", settings).await()
        } catch (e: Exception) {
            // Ignore
        }
    }
    
    suspend fun unlockAchievement(userId: String, achievementId: String) {
        try {
            val userRef = usersCollection.document(userId)
            
            firestore.runTransaction { transaction ->
                val user = transaction.get(userRef).toObject(UserProfile::class.java)
                
                if (user != null && !user.achievements.contains(achievementId)) {
                    val newAchievements = user.achievements + achievementId
                    transaction.update(userRef, "achievements", newAchievements)
                    
                    // Add coins reward
                    val achievement = getAchievementById(achievementId)
                    if (achievement != null) {
                        val newCoins = user.coins + achievement.coinsReward
                        transaction.update(userRef, "coins", newCoins)
                    }
                }
            }.await()
        } catch (e: Exception) {
            // Ignore
        }
    }
    
    suspend fun checkAndUnlockAchievements(userId: String, userProfile: UserProfile) {
        val achievementsToUnlock = mutableListOf<String>()
        
        // Check first bomb
        if (userProfile.totalBombs == 1 && 
            !userProfile.achievements.contains(Constants.ACHIEVEMENT_FIRST_BOMB)) {
            achievementsToUnlock.add(Constants.ACHIEVEMENT_FIRST_BOMB)
        }
        
        // Check 7 day streak
        if (userProfile.streak >= 7 && 
            !userProfile.achievements.contains(Constants.ACHIEVEMENT_STREAK_7)) {
            achievementsToUnlock.add(Constants.ACHIEVEMENT_STREAK_7)
        }
        
        // Check 100 bombs
        if (userProfile.totalBombs >= 100 && 
            !userProfile.achievements.contains(Constants.ACHIEVEMENT_100_BOMBS)) {
            achievementsToUnlock.add(Constants.ACHIEVEMENT_100_BOMBS)
        }
        
        achievementsToUnlock.forEach { achievementId ->
            unlockAchievement(userId, achievementId)
        }
    }
    
    private fun getYesterdayDate(): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
    }
    
    private fun getAchievementById(id: String): Achievement? {
        return when (id) {
            Constants.ACHIEVEMENT_FIRST_BOMB -> Achievement(
                id = id,
                title = "Первая бомба",
                description = "Отправь свою первую бомбу",
                icon = "🎉",
                coinsReward = 10
            )
            Constants.ACHIEVEMENT_STREAK_7 -> Achievement(
                id = id,
                title = "Огненная неделя",
                description = "7 дней подряд",
                icon = "🔥",
                coinsReward = 50
            )
            Constants.ACHIEVEMENT_TOP_1 -> Achievement(
                id = id,
                title = "Король дня",
                description = "Стань первым в топе",
                icon = "👑",
                coinsReward = 100
            )
            Constants.ACHIEVEMENT_100_BOMBS -> Achievement(
                id = id,
                title = "Бомбер",
                description = "100 бомб отправлено",
                icon = "💣",
                coinsReward = 200
            )
            else -> null
        }
    }
}
