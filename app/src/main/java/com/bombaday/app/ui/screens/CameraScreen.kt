package com.bombaday.app.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bombaday.app.model.SessionStatus
import com.bombaday.app.ui.theme.*
import com.bombaday.app.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentSession by viewModel.currentSession.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val userBomb by viewModel.userBomb.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()
    
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var isBlurred by remember { mutableStateOf(true) }
    
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    )
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedMediaUri = uri
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            currentSession?.status == SessionStatus.CAPTURING && userBomb == null -> {
                if (!permissionsState.allPermissionsGranted) {
                    PermissionRequiredScreen(
                        onRequestPermissions = { permissionsState.launchMultiplePermissionRequest() }
                    )
                } else {
                    if (selectedMediaUri == null) {
                        CaptureOptionsScreen(
                            timeRemaining = timeRemaining,
                            onGalleryClick = { galleryLauncher.launch("image/*") },
                            onCameraClick = { /* TODO: Open camera */ }
                        )
                    } else {
                        PreviewScreen(
                            mediaUri = selectedMediaUri!!,
                            isBlurred = isBlurred,
                            onBlurToggle = { isBlurred = !isBlurred },
                            onRetake = { selectedMediaUri = null },
                            onSend = {
                                viewModel.uploadBomb(
                                    mediaUri = selectedMediaUri!!,
                                    isVideo = false,
                                    isBlurred = isBlurred
                                )
                                selectedMediaUri = null
                            },
                            isUploading = uploadProgress
                        )
                    }
                }
            }
            
            userBomb != null -> {
                AlreadySubmittedScreen(timeRemaining = timeRemaining)
            }
            
            else -> {
                WaitingScreen(currentSession = currentSession)
            }
        }
    }
}

@Composable
fun CaptureOptionsScreen(
    timeRemaining: Long,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "💣 БОМБА ЗАПУЩЕНА!",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "${timeRemaining}",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Accent
        )
        
        Text(
            text = "секунд осталось!",
            style = MaterialTheme.typography.titleLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onCameraClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Camera, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "📸 СНЯТЬ ФОТО",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            OutlinedButton(
                onClick = onGalleryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "🖼️ ИЗ ГАЛЕРЕИ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PreviewScreen(
    mediaUri: Uri,
    isBlurred: Boolean,
    onBlurToggle: () -> Unit,
    onRetake: () -> Unit,
    onSend: () -> Unit,
    isUploading: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = mediaUri,
            contentDescription = "Preview",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(OverlayDark)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎭 Размыть лица",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isBlurred,
                    onCheckedChange = { onBlurToggle() }
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRetake,
                    modifier = Modifier.weight(1f).height(56.dp),
                    enabled = !isUploading
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Переснять")
                }
                
                Button(
                    onClick = onSend,
                    modifier = Modifier.weight(2f).height(56.dp),
                    enabled = !isUploading,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TextPrimary
                        )
                    } else {
                        Text(
                            text = "💣 ОТПРАВИТЬ БОМБУ",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlreadySubmittedScreen(timeRemaining: Long) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "✅",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Бомба отправлена!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Жди окончания голосования чтобы узнать результаты",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            if (timeRemaining > 0) {
                Text(
                    text = "Окно загрузки закроется через $timeRemaining сек",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Accent
                )
            }
        }
    }
}

@Composable
fun WaitingScreen(currentSession: com.bombaday.app.model.BombSession?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "💤",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Следующая бомба скоро...",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Мы пришлем тебе уведомление когда начнется!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PermissionRequiredScreen(onRequestPermissions: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "📷",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Нужен доступ к камере",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Чтобы снимать крутые бомбы!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            
            Button(
                onClick = onRequestPermissions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Разрешить", fontWeight = FontWeight.Bold)
            }
        }
    }
}
