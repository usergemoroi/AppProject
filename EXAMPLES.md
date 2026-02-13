# 📚 Примеры использования и кода

Этот файл содержит практические примеры использования различных компонентов приложения.

---

## 🎨 Примеры UI компонентов

### Кастомная карточка бомбы с новым дизайном

```kotlin
@Composable
fun CustomBombCard(bomb: Bomb) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            // Изображение бомбы
            AsyncImage(
                model = bomb.mediaUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            
            // Градиентный оверлей снизу
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
            
            // Позиция в топе (если есть)
            if (bomb.position > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text("#${bomb.position}")
                }
            }
            
            // Реакции внизу
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EmojiReaction("🔥", bomb.getFireScore())
                EmojiReaction("💀", bomb.getSkullScore())
                EmojiReaction("🤡", bomb.getClownScore())
            }
        }
    }
}

@Composable
fun EmojiReaction(emoji: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(emoji, fontSize = 24.sp)
        Text(
            text = count.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
```

### Анимированный таймер обратного отсчета

```kotlin
@Composable
fun AnimatedCountdownTimer(timeRemaining: Long) {
    val animatedProgress = remember { Animatable(1f) }
    
    LaunchedEffect(timeRemaining) {
        animatedProgress.animateTo(
            targetValue = timeRemaining / 60f,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    
    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Круговой прогресс
        CircularProgressIndicator(
            progress = animatedProgress.value,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 8.dp,
            color = Primary
        )
        
        // Текст таймера
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeRemaining.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Accent
            )
            Text(
                text = "секунд",
                fontSize = 16.sp,
                color = TextSecondary
            )
        }
    }
}
```

### Карусель стикеров

```kotlin
@Composable
fun StickerCarousel(
    stickers: List<Int>,
    onStickerSelected: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stickers) { stickerId ->
            Image(
                painter = painterResource(stickerId),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Primary, CircleShape)
                    .clickable { onStickerSelected(stickerId) }
                    .padding(8.dp)
            )
        }
    }
}
```

---

## 🔄 Примеры работы с данными

### Загрузка и компрессия изображения

```kotlin
suspend fun uploadImageWithCompression(
    context: Context,
    uri: Uri,
    sessionId: String
): Result<String> {
    return withContext(Dispatchers.IO) {
        try {
            // 1. Загружаем bitmap
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(context.contentResolver, uri)
                )
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            
            // 2. Компрессия через C++
            val compressedData = VideoCompressor.compressImage(bitmap, quality = 75)
            
            if (compressedData == null) {
                return@withContext Result.failure(Exception("Compression failed"))
            }
            
            // 3. Сохраняем во временный файл
            val tempFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { it.write(compressedData) }
            
            // 4. Загружаем в Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference
                .child("bombs/$sessionId/${UUID.randomUUID()}.jpg")
            
            val uploadTask = storageRef.putFile(Uri.fromFile(tempFile))
            
            uploadTask.await()
            val downloadUrl = storageRef.downloadUrl.await()
            
            // 5. Удаляем временный файл
            tempFile.delete()
            
            Result.success(downloadUrl.toString())
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Реалтайм обновление топа

```kotlin
@Composable
fun LiveTopBombs(sessionId: String) {
    val topBombs = remember { mutableStateListOf<Bomb>() }
    
    LaunchedEffect(sessionId) {
        val listener = FirebaseFirestore.getInstance()
            .collection("bombs")
            .whereEqualTo("bombSessionId", sessionId)
            .orderBy("totalVotes", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                
                topBombs.clear()
                topBombs.addAll(
                    snapshot.documents.mapNotNull { 
                        it.toObject(Bomb::class.java) 
                    }
                )
            }
        
        // Cleanup
        awaitCancellation()
    }
    
    LazyColumn {
        itemsIndexed(topBombs) { index, bomb ->
            BombCard(
                bomb = bomb.copy(position = index + 1),
                onEmojiClick = { emoji -> /* vote */ },
                onBombClick = { /* open detail */ }
            )
        }
    }
}
```

### Пагинация для больших списков

```kotlin
class BombPagingSource(
    private val sessionId: String
) : PagingSource<DocumentSnapshot, Bomb>() {
    
    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Bomb> {
        return try {
            val query = FirebaseFirestore.getInstance()
                .collection("bombs")
                .whereEqualTo("bombSessionId", sessionId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())
            
            val snapshot = if (params.key != null) {
                query.startAfter(params.key!!).get().await()
            } else {
                query.get().await()
            }
            
            val bombs = snapshot.documents.mapNotNull { it.toObject(Bomb::class.java) }
            
            LoadResult.Page(
                data = bombs,
                prevKey = null,
                nextKey = snapshot.documents.lastOrNull()
            )
            
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Bomb>): DocumentSnapshot? {
        return null
    }
}

// Использование в ViewModel:
val bombsPager = Pager(
    config = PagingConfig(pageSize = 20),
    pagingSourceFactory = { BombPagingSource(sessionId) }
).flow.cachedIn(viewModelScope)
```

---

## 🎥 Примеры работы с камерой

### CameraX Preview с захватом фото

```kotlin
@Composable
fun CameraPreview(
    onPhotoTaken: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                
            }, ContextCompat.getMainExecutor(ctx))
            
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        FloatingActionButton(
            onClick = {
                val photoFile = File(
                    context.externalCacheDir,
                    "photo_${System.currentTimeMillis()}.jpg"
                )
                
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                
                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            onPhotoTaken(Uri.fromFile(photoFile))
                        }
                        
                        override fun onError(exception: ImageCaptureException) {
                            exception.printStackTrace()
                        }
                    }
                )
            },
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(Icons.Default.Camera, contentDescription = "Take photo")
        }
    }
}
```

### Запись видео (5 секунд)

```kotlin
@Composable
fun VideoRecorder(onVideoRecorded: (Uri) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var recording by remember { mutableStateOf<Recording?>(null) }
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    
    DisposableEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        
        videoCapture = VideoCapture.withOutput(recorder)
        
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            videoCapture
        )
        
        onDispose {
            cameraProvider.unbindAll()
        }
    }
    
    Button(
        onClick = {
            val videoFile = File(
                context.externalCacheDir,
                "video_${System.currentTimeMillis()}.mp4"
            )
            
            val outputOptions = FileOutputOptions.Builder(videoFile).build()
            
            recording = videoCapture?.output
                ?.prepareRecording(context, outputOptions)
                ?.start(ContextCompat.getMainExecutor(context)) { event ->
                    when (event) {
                        is VideoRecordEvent.Start -> {
                            // Автоматически останавливаем через 5 секунд
                            Handler(Looper.getMainLooper()).postDelayed({
                                recording?.stop()
                            }, 5000)
                        }
                        is VideoRecordEvent.Finalize -> {
                            if (!event.hasError()) {
                                onVideoRecorded(Uri.fromFile(videoFile))
                            }
                        }
                    }
                }
        }
    ) {
        Text(if (recording != null) "Запись..." else "Записать видео")
    }
}
```

---

## 🔔 Примеры работы с уведомлениями

### Запрос разрешения на уведомления (Android 13+)

```kotlin
@Composable
fun RequestNotificationPermission(onPermissionGranted: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            }
        }
        
        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    } else {
        LaunchedEffect(Unit) {
            onPermissionGranted()
        }
    }
}
```

### Подписка на FCM топики

```kotlin
fun subscribeToUserTopic(userId: String) {
    FirebaseMessaging.getInstance().apply {
        subscribeToTopic("all_users")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to all_users")
                }
            }
        
        subscribeToTopic("user_$userId")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to user topic")
                }
            }
    }
}
```

### Локальное уведомление (без FCM)

```kotlin
fun showLocalNotification(
    context: Context,
    title: String,
    message: String,
    channelId: String = "bomb_launch"
) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_bomb)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()
    
    notificationManager.notify(Random.nextInt(), notification)
}
```

---

## 🎯 Примеры аналитики

### Логирование событий с параметрами

```kotlin
fun logBombUploadEvent(
    sessionId: String,
    mediaType: String,
    hasBlur: Boolean,
    uploadDuration: Long
) {
    Firebase.analytics.logEvent("bomb_uploaded") {
        param("session_id", sessionId)
        param("media_type", mediaType)
        param("has_blur", if (hasBlur) "yes" else "no")
        param("upload_duration_ms", uploadDuration)
        param(FirebaseAnalytics.Param.VALUE, 1.0)
    }
}

fun logVoteEvent(bombId: String, emoji: String, fromTopFeed: Boolean) {
    Firebase.analytics.logEvent("vote_cast") {
        param("bomb_id", bombId)
        param("emoji", emoji)
        param("from_top_feed", if (fromTopFeed) "yes" else "no")
    }
}

fun logScreenView(screenName: String) {
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
    }
}

fun logPurchaseEvent(sku: String, price: Double) {
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
        param(FirebaseAnalytics.Param.ITEM_ID, sku)
        param(FirebaseAnalytics.Param.CURRENCY, "RUB")
        param(FirebaseAnalytics.Param.VALUE, price)
    }
}
```

---

## 🔐 Примеры безопасности

### Проверка размера файла перед загрузкой

```kotlin
fun validateFileSize(context: Context, uri: Uri): Boolean {
    val maxSizeBytes = Constants.MAX_FILE_SIZE_MB * 1024 * 1024
    
    val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.use {
        it.statSize
    } ?: 0
    
    return fileSize <= maxSizeBytes
}
```

### Валидация контента перед сохранением

```kotlin
fun validateBombData(bomb: Bomb): Boolean {
    return bomb.mediaUrl.isNotEmpty() &&
           bomb.deviceId.isNotEmpty() &&
           bomb.bombSessionId.isNotEmpty() &&
           bomb.mediaUrl.startsWith("https://")
}
```

---

## 🎨 Примеры тем и стилей

### Кастомная цветовая схема

```kotlin
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Accent,
    background = Color.White,
    surface = Color(0xFFF5F5F5)
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Accent,
    background = Background,
    surface = Surface
)

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
```

---

Эти примеры помогут вам быстро добавить новый функционал в приложение!
