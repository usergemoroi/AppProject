package com.bombaday.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bombaday.app.model.SessionStatus
import com.bombaday.app.ui.components.BombCard
import com.bombaday.app.ui.theme.*
import com.bombaday.app.viewmodel.MainViewModel

@Composable
fun FeedScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentSession by viewModel.currentSession.collectAsState()
    val topBombs by viewModel.topBombs.collectAsState()
    val allBombs by viewModel.allBombs.collectAsState()
    val userBomb by viewModel.userBomb.collectAsState()
    val selectedEmojis by viewModel.selectedEmojis.collectAsState()
    val votingTimeRemaining by viewModel.votingTimeRemaining.collectAsState()
    
    var showTopOnly by remember { mutableStateOf(true) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Surface,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showTopOnly) "🔥 ТОП БОМБЫ ДНЯ" else "ВСЕ БОМБЫ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Primary
                        )
                    }
                }
                
                when (currentSession?.status) {
                    SessionStatus.VOTING -> {
                        val minutes = votingTimeRemaining / 60
                        val seconds = votingTimeRemaining % 60
                        
                        Text(
                            text = "⏰ Голосование закончится через: $minutes:${seconds.toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Accent,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    SessionStatus.COMPLETED -> {
                        Text(
                            text = "✅ Результаты финальные!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Success,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    else -> {}
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = showTopOnly,
                        onClick = { showTopOnly = true },
                        label = { Text("Топ-10") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !showTopOnly,
                        onClick = { showTopOnly = false },
                        label = { Text("Все") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                if (userBomb != null) {
                    val position = topBombs.indexOfFirst { it.id == userBomb?.id }
                    val positionText = if (position >= 0) "#${position + 1}" else "Не в топе"
                    
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceVariant
                    ) {
                        Text(
                            text = "Твоя позиция: $positionText",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
        
        val displayBombs = if (showTopOnly) topBombs else allBombs
        
        if (displayBombs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "😴",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = "Пока нет бомб",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextSecondary
                    )
                    Text(
                        text = "Жди следующей бомбы!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayBombs) { bomb ->
                    BombCard(
                        bomb = bomb,
                        position = if (showTopOnly) displayBombs.indexOf(bomb) + 1 else null,
                        selectedEmoji = selectedEmojis[bomb.id],
                        onEmojiClick = { emoji ->
                            if (currentSession?.status == SessionStatus.VOTING) {
                                viewModel.voteBomb(bomb.id, emoji)
                            }
                        },
                        onBombClick = { }
                    )
                }
            }
        }
    }
}
