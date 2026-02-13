package com.bombaday.app.model

import com.google.firebase.Timestamp

data class Bomb(
    val id: String = "",
    val userId: String = "",
    val mediaUrl: String = "",
    val thumbnailUrl: String = "",
    val mediaType: MediaType = MediaType.IMAGE,
    val timestamp: Timestamp = Timestamp.now(),
    val location: BombLocation? = null,
    val isBlurred: Boolean = true,
    val reactions: Map<String, Int> = emptyMap(), // emoji -> count
    val totalVotes: Int = 0,
    val position: Int = 0,
    val isAnonymous: Boolean = true,
    val deviceId: String = "",
    val bombSessionId: String = "" // Daily session ID
) {
    fun getFireScore(): Int = reactions["🔥"] ?: 0
    fun getSkullScore(): Int = reactions["💀"] ?: 0
    fun getClownScore(): Int = reactions["🤡"] ?: 0
    
    fun getTotalScore(): Int = 
        (getFireScore() * 3) + (getSkullScore() * 2) + (getClownScore() * 1)
}

enum class MediaType {
    IMAGE,
    VIDEO
}

data class BombLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = "",
    val country: String = "Russia"
)

data class BombSession(
    val id: String = "",
    val date: String = "", // YYYY-MM-DD
    val launchTime: Timestamp = Timestamp.now(),
    val captureWindowSeconds: Int = 60,
    val votingWindowMinutes: Int = 5,
    val votingEndsAt: Timestamp = Timestamp.now(),
    val totalParticipants: Int = 0,
    val status: SessionStatus = SessionStatus.WAITING
)

enum class SessionStatus {
    WAITING,      // Waiting for next bomb
    CAPTURING,    // 60 second capture window
    VOTING,       // 5 minute voting window
    COMPLETED     // Results are final
}

data class UserProfile(
    val userId: String = "",
    val deviceId: String = "",
    val streak: Int = 0,
    val coins: Int = 0,
    val totalBombs: Int = 0,
    val topTenCount: Int = 0,
    val achievements: List<String> = emptyList(),
    val isPro: Boolean = false,
    val lastBombDate: String = "",
    val settings: UserSettings = UserSettings()
)

data class UserSettings(
    val notificationsEnabled: Boolean = true,
    val locationRadius: LocationRadius = LocationRadius.CITY,
    val anonymousMode: Boolean = true,
    val autoBlur: Boolean = true
)

enum class LocationRadius(val displayName: String, val radiusKm: Double) {
    NEARBY("10 км", 10.0),
    CITY("Город", 50.0),
    COUNTRY("Страна", 5000.0),
    GLOBAL("Весь мир", Double.MAX_VALUE)
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val coinsReward: Int,
    val isUnlocked: Boolean = false
)

data class Vote(
    val bombId: String = "",
    val userId: String = "",
    val emoji: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
