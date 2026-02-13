package com.bombaday.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bombaday.app.ui.theme.*
import com.bombaday.app.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Surface
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "💣",
                        style = MaterialTheme.typography.displayMedium
                    )
                    
                    Text(
                        text = "БомбаДня Профиль",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            icon = "🔥",
                            value = userProfile?.streak?.toString() ?: "0",
                            label = "Стрик"
                        )
                        StatItem(
                            icon = "💰",
                            value = userProfile?.coins?.toString() ?: "0",
                            label = "Бабки"
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            icon = "💣",
                            value = userProfile?.totalBombs?.toString() ?: "0",
                            label = "Бомб"
                        )
                        StatItem(
                            icon = "🏆",
                            value = userProfile?.topTenCount?.toString() ?: "0",
                            label = "В топ-10"
                        )
                    }
                }
            }
        }
        
        item {
            if (userProfile?.isPro == false) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Primary
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "💎 АПГРЕЙД ДО PRO",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "✓ Без рекламы\n✓ Приоритет в голосовании\n✓ Кастомные темы\n✓ Двойной таймер\n✓ Эксклюзивные стикеры",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.ui.graphics.Color.Black
                            )
                        ) {
                            Text("Получить PRO за 299₽/мес")
                        }
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Достижения",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Surface
            ) {
                Column {
                    AchievementItem(
                        icon = "🎉",
                        title = "Первая бомба",
                        description = "Отправь свою первую бомбу",
                        reward = "+10 бабок",
                        isUnlocked = userProfile?.achievements?.contains("first_bomb") ?: false
                    )
                    Divider()
                    AchievementItem(
                        icon = "🔥",
                        title = "Огненная неделя",
                        description = "7 дней подряд",
                        reward = "+50 бабок",
                        isUnlocked = userProfile?.achievements?.contains("streak_7") ?: false
                    )
                    Divider()
                    AchievementItem(
                        icon = "👑",
                        title = "Король дня",
                        description = "Стань первым в топе",
                        reward = "+100 бабок",
                        isUnlocked = userProfile?.achievements?.contains("top_1") ?: false
                    )
                    Divider()
                    AchievementItem(
                        icon = "💣",
                        title = "Бомбер",
                        description = "100 бомб отправлено",
                        reward = "+200 бабок",
                        isUnlocked = userProfile?.achievements?.contains("100_bombs") ?: false
                    )
                }
            }
        }
        
        item {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Surface
            ) {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Уведомления",
                        subtitle = "Пуши о новых бомбах"
                    )
                    Divider()
                    SettingsItem(
                        icon = Icons.Default.LocationOn,
                        title = "Локация",
                        subtitle = "Радиус показа: Город"
                    )
                    Divider()
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Приватность",
                        subtitle = "Анонимный режим включен"
                    )
                    Divider()
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "О приложении",
                        subtitle = "Версия 1.0.0"
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(
    icon: String,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun AchievementItem(
    icon: String,
    title: String,
    description: String,
    reward: String,
    isUnlocked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .size(48.dp)
                .wrapContentSize(Alignment.Center)
        )
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) Primary else TextDisabled
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = reward,
                style = MaterialTheme.typography.bodySmall,
                color = Accent
            )
        }
        
        if (isUnlocked) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Unlocked",
                tint = Success
            )
        } else {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = TextDisabled
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp)
        )
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open",
            tint = TextSecondary
        )
    }
}
