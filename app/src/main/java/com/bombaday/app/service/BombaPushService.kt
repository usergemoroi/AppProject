package com.bombaday.app.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.bombaday.app.MainActivity
import com.bombaday.app.R
import com.bombaday.app.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BombaPushService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        remoteMessage.data.let { data ->
            val type = data["type"] ?: return
            
            when (type) {
                "bomb_launch" -> {
                    showBombLaunchNotification()
                }
                "voting_end" -> {
                    val position = data["position"]?.toIntOrNull()
                    showResultsNotification(position)
                }
            }
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send token to server
    }
    
    private fun showBombLaunchNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_camera", true)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_BOMB)
            .setSmallIcon(R.drawable.ic_bomb)
            .setContentTitle("💣 БОМБА ЗАПУЩЕНА!")
            .setContentText("У тебя есть 60 секунд! Открывай камеру!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
    
    private fun showResultsNotification(position: Int?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val title = when {
            position == null -> "Голосование завершено"
            position == 1 -> "🏆 ТЫ ПЕРВЫЙ!"
            position <= 10 -> "🔥 ТЫ В ТОП-10!"
            else -> "Результаты готовы"
        }
        
        val text = when {
            position == null -> "Смотри результаты в приложении"
            position == 1 -> "Поздравляем! Ты король дня!"
            position <= 10 -> "Твоя позиция: #$position"
            else -> "Продолжай бомбить! Следующая попытка скоро"
        }
        
        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_RESULTS)
            .setSmallIcon(R.drawable.ic_bomb)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}
