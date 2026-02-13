package com.bombaday.app.util

object Constants {
    // Firebase Collections
    const val COLLECTION_BOMBS = "bombs"
    const val COLLECTION_SESSIONS = "bomb_sessions"
    const val COLLECTION_USERS = "users"
    const val COLLECTION_VOTES = "votes"
    const val COLLECTION_ACHIEVEMENTS = "achievements"
    
    // Storage paths
    const val STORAGE_BOMBS = "bombs"
    const val STORAGE_THUMBNAILS = "thumbnails"
    
    // Timing
    const val CAPTURE_WINDOW_SECONDS = 60
    const val VOTING_WINDOW_MINUTES = 5
    const val VIDEO_MAX_DURATION_SECONDS = 5
    
    // Limits
    const val TOP_BOMBS_LIMIT = 10
    const val FEED_PAGE_SIZE = 20
    const val MAX_FILE_SIZE_MB = 50
    
    // Preferences
    const val PREF_DEVICE_ID = "device_id"
    const val PREF_USER_ID = "user_id"
    const val PREF_LAST_BOMB_DATE = "last_bomb_date"
    const val PREF_STREAK = "streak"
    
    // Notifications
    const val NOTIFICATION_CHANNEL_BOMB = "bomb_launch"
    const val NOTIFICATION_CHANNEL_RESULTS = "bomb_results"
    
    // Pro features
    const val PRO_PRICE_MONTHLY = 299
    
    // Achievements IDs
    const val ACHIEVEMENT_FIRST_BOMB = "first_bomb"
    const val ACHIEVEMENT_STREAK_7 = "streak_7"
    const val ACHIEVEMENT_TOP_1 = "top_1"
    const val ACHIEVEMENT_100_BOMBS = "100_bombs"
    
    // Emoji reactions
    const val EMOJI_FIRE = "🔥"
    const val EMOJI_SKULL = "💀"
    const val EMOJI_CLOWN = "🤡"
    
    val AVAILABLE_EMOJIS = listOf(EMOJI_FIRE, EMOJI_SKULL, EMOJI_CLOWN)
}
