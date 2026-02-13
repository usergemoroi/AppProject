package com.bombaday.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bombaday.app.model.Bomb
import com.bombaday.app.model.MediaType
import com.bombaday.app.ui.theme.*
import com.bombaday.app.util.Constants

@Composable
fun BombCard(
    bomb: Bomb,
    position: Int? = null,
    selectedEmoji: String? = null,
    onEmojiClick: (String) -> Unit,
    onBombClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBombClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = bomb.thumbnailUrl.ifEmpty { bomb.mediaUrl },
                    contentDescription = "Bomb media",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                if (bomb.mediaType == MediaType.VIDEO) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                            .background(OverlayDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = TextPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                if (position != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .background(
                                when {
                                    position == 1 -> Color(0xFFFFD700)
                                    position == 2 -> Color(0xFFC0C0C0)
                                    position == 3 -> Color(0xFFCD7F32)
                                    else -> Primary
                                },
                                CircleShape
                            )
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "#$position",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Constants.AVAILABLE_EMOJIS.forEach { emoji ->
                        EmojiButton(
                            emoji = emoji,
                            count = when (emoji) {
                                Constants.EMOJI_FIRE -> bomb.getFireScore()
                                Constants.EMOJI_SKULL -> bomb.getSkullScore()
                                Constants.EMOJI_CLOWN -> bomb.getClownScore()
                                else -> 0
                            },
                            isSelected = selectedEmoji == emoji,
                            onClick = { onEmojiClick(emoji) }
                        )
                    }
                }
                
                Text(
                    text = "${bomb.totalVotes} голосов",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun EmojiButton(
    emoji: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Primary else SurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleMedium
            )
            if (count > 0) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) TextPrimary else TextSecondary
                )
            }
        }
    }
}
