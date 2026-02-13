package com.bombaday.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bombaday.app.util.Constants
import com.google.firebase.FirebaseApp

class BombaDayApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        FirebaseApp.initializeApp(this)
        
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val bombChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_BOMB,
                "Запуск бомбы",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о запуске новой бомбы"
                enableVibration(true)
            }
            
            val resultsChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_RESULTS,
                "Результаты",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Результаты голосования"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(bombChannel)
            notificationManager.createNotificationChannel(resultsChannel)
        }
    }
}
