# 💣 БомбаДня - Android приложение

**БомбаДня** - это социальное приложение, где каждый день в случайное время все пользователи одновременно имеют **ровно 60 секунд**, чтобы загрузить самую тупую/стыдную/смешную/трэшовую фотку или видео. Всё анонимно, с гео-фильтром, голосованием эмодзи и топ-10 "бомб дня".

---

## 🎯 Основные возможности

- ⏱️ **60-секундное окно** для загрузки контента каждый день
- 📸 **Фото или 5-секундное видео** из камеры или галереи
- 🎭 **Анонимность** - без лиц по умолчанию, опциональный блюр
- 🗺️ **Гео-фильтры** - радиус 10 км / город / страна / глобал
- 🔥💀🤡 **Голосование эмодзи** - 5 минут после окна загрузки
- 🏆 **Топ-10 бомб дня** - глобальная лента и чаты друзей
- 📈 **Стрики и достижения** - виртуальные "бабки" и бонусы
- 💎 **PRO-версия** - без рекламы, приоритет, кастомные темы

---

## 🛠️ Технологический стек

### Основной стек
- **Язык:** Kotlin 1.9.20
- **UI:** Jetpack Compose (Material 3)
- **Минимальный Android:** 7.0 (API 24)
- **Целевой Android:** 14 (API 34)

### Основные библиотеки
- **Jetpack Compose** - современный декларативный UI
- **CameraX** - работа с камерой
- **Media3** - воспроизведение видео
- **Firebase:**
  - Firestore - база данных
  - Storage - хранение медиа
  - Auth - аутентификация
  - Cloud Messaging - пуш-уведомления
  - Analytics - аналитика
- **Coil** - загрузка и кэширование изображений
- **DataStore** - локальное хранение настроек
- **WorkManager** - фоновые задачи
- **Location Services** - геолокация
- **C++ NDK** - оптимизация компрессии видео

---

## 📁 Структура проекта

```
BombaDay/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/bombaday/app/
│   │   │   │   ├── model/              # Модели данных
│   │   │   │   │   └── Bomb.kt
│   │   │   │   ├── repository/         # Работа с данными
│   │   │   │   │   ├── BombRepository.kt
│   │   │   │   │   └── UserRepository.kt
│   │   │   │   ├── viewmodel/          # Бизнес-логика
│   │   │   │   │   └── MainViewModel.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/          # Темы и стили
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   ├── components/     # Переиспользуемые UI
│   │   │   │   │   │   └── BombCard.kt
│   │   │   │   │   └── screens/        # Экраны приложения
│   │   │   │   │       ├── FeedScreen.kt
│   │   │   │   │       ├── CameraScreen.kt
│   │   │   │   │       └── ProfileScreen.kt
│   │   │   │   ├── service/            # Сервисы
│   │   │   │   │   └── BombaPushService.kt
│   │   │   │   ├── util/               # Утилиты
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── NativeLib.kt
│   │   │   │   │   └── VideoCompressor.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── BombaDayApplication.kt
│   │   │   ├── cpp/                    # C++ оптимизации
│   │   │   │   ├── CMakeLists.txt
│   │   │   │   ├── native-lib.cpp
│   │   │   │   └── video_compressor.cpp
│   │   │   ├── res/
│   │   │   │   ├── drawable/           # Векторные иконки
│   │   │   │   ├── mipmap-*/           # Иконки приложения
│   │   │   │   ├── values/             # Строки, цвета, темы
│   │   │   │   └── xml/                # Конфигурация
│   │   │   └── AndroidManifest.xml
│   │   └── google-services.json        # Firebase конфиг
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## 🚀 Начало работы

### 1. Предварительные требования

- **Android Studio** (Arctic Fox или новее) - [Скачать](https://developer.android.com/studio)
- **JDK 17** или выше
- **Android SDK** с API 24-34
- **Firebase проект** (см. настройку ниже)

### 2. Клонирование репозитория

```bash
git clone https://github.com/yourusername/bombaday.git
cd bombaday
```

### 3. Настройка Firebase

#### 3.1 Создание Firebase проекта

1. Перейдите на [Firebase Console](https://console.firebase.google.com/)
2. Нажмите "Добавить проект"
3. Введите название проекта: `BombaDay`
4. Выберите регион (рекомендуется: Europe)
5. Включите Google Analytics (опционально)

#### 3.2 Добавление Android приложения

1. В Firebase Console выберите проект
2. Нажмите на иконку Android
3. Введите package name: `com.bombaday.app`
4. Введите nickname приложения: `BombaDay`
5. Скачайте файл `google-services.json`
6. Поместите файл в `app/` директорию (замените example файл)

#### 3.3 Настройка Firebase Services

##### Firestore Database

1. В Firebase Console: Build → Firestore Database
2. Нажмите "Создать базу данных"
3. Выберите режим "Production"
4. Выберите регион (europe-west3 для России)
5. Создайте следующие коллекции:
   - `bombs` - хранение бомб
   - `bomb_sessions` - сессии дня
   - `users` - профили пользователей
   - `votes` - голоса
   - `achievements` - достижения

##### Правила безопасности Firestore:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Bombs - все могут читать, только владелец может создавать
    match /bombs/{bombId} {
      allow read: if true;
      allow create: if request.auth != null || request.resource.data.deviceId != null;
      allow update: if false;
      allow delete: if false;
    }
    
    // Sessions - все могут читать
    match /bomb_sessions/{sessionId} {
      allow read: if true;
      allow write: if false; // Только через Cloud Functions
    }
    
    // Users - только владелец может читать/писать
    match /users/{userId} {
      allow read: if request.auth != null && request.auth.uid == userId;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Votes - можно создать, но не изменить
    match /votes/{voteId} {
      allow read: if true;
      allow create: if request.auth != null || request.resource.data.userId != null;
      allow update, delete: if false;
    }
    
    // Achievements - только чтение
    match /achievements/{achievementId} {
      allow read: if true;
      allow write: if false;
    }
  }
}
```

##### Firebase Storage

1. В Firebase Console: Build → Storage
2. Нажмите "Начать"
3. Выберите режим "Production"
4. Выберите регион (europe-west3)

##### Правила безопасности Storage:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /bombs/{sessionId}/{fileName} {
      allow read: if true;
      allow write: if request.resource.size < 50 * 1024 * 1024 // 50MB limit
                   && (request.resource.contentType.matches('image/.*') 
                       || request.resource.contentType.matches('video/.*'));
    }
    
    match /thumbnails/{sessionId}/{fileName} {
      allow read: if true;
      allow write: if request.resource.size < 5 * 1024 * 1024; // 5MB limit
    }
  }
}
```

##### Cloud Messaging (FCM)

1. В Firebase Console: Build → Cloud Messaging
2. Включите Cloud Messaging API
3. Сохраните Server Key (для отправки пушей)

##### Authentication (опционально)

1. В Firebase Console: Build → Authentication
2. Нажмите "Начать"
3. Включите "Anonymous" authentication
4. Это позволит генерировать уникальные ID для устройств

### 4. Настройка Android Studio

#### 4.1 Открытие проекта

1. Запустите Android Studio
2. File → Open → выберите папку `bombaday`
3. Дождитесь синхронизации Gradle (может занять несколько минут)

#### 4.2 Настройка NDK (для C++ компиляции)

1. Tools → SDK Manager
2. SDK Tools tab
3. Установите:
   - NDK (Side by side)
   - CMake

#### 4.3 Синхронизация зависимостей

```bash
./gradlew build
```

### 5. Запуск приложения

#### 5.1 На эмуляторе

1. Tools → Device Manager
2. Create Device
3. Выберите Pixel 5 или новее
4. System Image: API 33 (Android 13) или выше
5. Finish
6. Run → Run 'app' (или Shift+F10)

#### 5.2 На реальном устройстве

1. Включите режим разработчика на устройстве:
   - Настройки → О телефоне → Нажмите 7 раз на "Номер сборки"
2. Включите отладку по USB:
   - Настройки → Для разработчиков → Отладка по USB
3. Подключите устройство к компьютеру
4. Run → Run 'app'

---

## 🎨 Кастомизация и дополнения

### Изменение цветовой схемы

Отредактируйте файл `app/src/main/java/com/bombaday/app/ui/theme/Color.kt`:

```kotlin
val Primary = Color(0xFFFF6B35)      // Основной цвет
val Accent = Color(0xFFFFD23F)       // Акцентный цвет
val Background = Color(0xFF000000)   // Фон
```

### Добавление новых эмодзи реакций

1. Откройте `app/src/main/java/com/bombaday/app/util/Constants.kt`
2. Добавьте новые эмодзи в `AVAILABLE_EMOJIS`:

```kotlin
const val EMOJI_HEART = "❤️"
val AVAILABLE_EMOJIS = listOf(EMOJI_FIRE, EMOJI_SKULL, EMOJI_CLOWN, EMOJI_HEART)
```

3. Обновите модель `Bomb.kt` для обработки нового эмодзи

### Создание собственных иконок

Иконки приложения находятся в `app/src/main/res/mipmap-*`. Замените PNG файлы:

- `ic_launcher.png` - обычная иконка
- `ic_launcher_round.png` - круглая иконка

Рекомендуемые размеры:
- mdpi: 48x48
- hdpi: 72x72
- xhdpi: 96x96
- xxhdpi: 144x144
- xxxhdpi: 192x192

**Инструменты для создания иконок:**
- [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/)
- [Figma](https://www.figma.com/)
- [Canva](https://www.canva.com/)

### Добавление новых достижений

1. Откройте `UserRepository.kt`
2. Добавьте новое достижение в `getAchievementById()`:

```kotlin
const val ACHIEVEMENT_NEW = "new_achievement"

private fun getAchievementById(id: String): Achievement? {
    return when (id) {
        // ... existing achievements
        ACHIEVEMENT_NEW -> Achievement(
            id = id,
            title = "Новое достижение",
            description = "Описание",
            icon = "🎯",
            coinsReward = 100
        )
        else -> null
    }
}
```

3. Добавьте логику проверки в `checkAndUnlockAchievements()`

---

## 🔧 Настройка пуш-уведомлений

### Cloud Functions для автоматических бомб

Создайте Cloud Function для запуска ежедневных бомб:

```javascript
// functions/index.js
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.scheduleDailyBomb = functions.pubsub
  .schedule('0 19 * * *') // 19:00 каждый день (настройте время)
  .timeZone('Europe/Moscow')
  .onRun(async (context) => {
    const sessionId = `bomb_${Date.now()}`;
    const now = admin.firestore.Timestamp.now();
    
    // Создаем новую сессию
    await admin.firestore().collection('bomb_sessions').doc(sessionId).set({
      id: sessionId,
      date: new Date().toISOString().split('T')[0],
      launchTime: now,
      captureWindowSeconds: 60,
      votingWindowMinutes: 5,
      votingEndsAt: new admin.firestore.Timestamp(now.seconds + 360, 0), // +6 min
      totalParticipants: 0,
      status: 'CAPTURING'
    });
    
    // Отправляем пуш всем пользователям
    const message = {
      notification: {
        title: '💣 БОМБА ЗАПУЩЕНА!',
        body: 'У тебя есть 60 секунд! Открывай камеру!'
      },
      data: {
        type: 'bomb_launch',
        sessionId: sessionId
      },
      topic: 'all_users'
    };
    
    await admin.messaging().send(message);
    
    return null;
  });

exports.endVoting = functions.pubsub
  .schedule('every 5 minutes')
  .onRun(async (context) => {
    const now = admin.firestore.Timestamp.now();
    
    // Находим сессии, где голосование закончилось
    const sessions = await admin.firestore()
      .collection('bomb_sessions')
      .where('status', '==', 'VOTING')
      .where('votingEndsAt', '<=', now)
      .get();
    
    for (const doc of sessions.docs) {
      await doc.ref.update({ status: 'COMPLETED' });
      
      // Получаем топ-10 и отправляем уведомления
      const bombs = await admin.firestore()
        .collection('bombs')
        .where('bombSessionId', '==', doc.id)
        .orderBy('totalVotes', 'desc')
        .limit(10)
        .get();
      
      // TODO: Отправить уведомления пользователям об их позициях
    }
    
    return null;
  });
```

Деплой функций:

```bash
npm install -g firebase-tools
firebase login
firebase init functions
cd functions
npm install
firebase deploy --only functions
```

### Подписка на топик в приложении

В `MainActivity.onCreate()` добавьте:

```kotlin
FirebaseMessaging.getInstance().subscribeToTopic("all_users")
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("FCM", "Subscribed to topic")
        }
    }
```

---

## 📱 Поддержка Android версий

Приложение поддерживает Android 7.0 (API 24) до Android 14 (API 34).

### Особенности для разных версий:

**Android 7-8 (API 24-26):**
- Базовый функционал
- Возможны ограничения в разрешениях

**Android 9-10 (API 28-29):**
- Полная поддержка
- Scoped Storage начинает работать

**Android 11+ (API 30+):**
- Полная поддержка новых разрешений
- READ_MEDIA_IMAGES, READ_MEDIA_VIDEO

**Android 12+ (API 31+):**
- Material You динамические цвета (не реализовано)
- Новые пуш-уведомления требуют разрешения

**Android 13+ (API 33+):**
- Granular media permissions
- POST_NOTIFICATIONS разрешение

---

## 🐛 Отладка и тестирование

### Логирование

Просмотр логов в реальном времени:

```bash
adb logcat | grep BombaDay
```

Фильтр по тегу:

```bash
adb logcat -s BombaDay-Native
```

### Тестирование без Firebase

Для локального тестирования используйте Firebase Emulator Suite:

```bash
firebase emulators:start
```

В коде подключитесь к эмуляторам:

```kotlin
// В BombaDayApplication.onCreate()
if (BuildConfig.DEBUG) {
    FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080)
    FirebaseStorage.getInstance().useEmulator("10.0.2.2", 9199)
}
```

### Тестирование пушей

Отправка тестового пуша через Firebase Console:
1. Cloud Messaging → New notification
2. Notification text: "Test"
3. Target: Topic "all_users"
4. Additional options → Custom data:
   - Key: `type`, Value: `bomb_launch`

---

## 🚀 Сборка Release версии

### 1. Создание ключа подписи

```bash
keytool -genkey -v -keystore bombaday-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias bombaday
```

Сохраните пароль и alias!

### 2. Настройка подписи в Android Studio

1. Build → Generate Signed Bundle / APK
2. Выберите Android App Bundle (рекомендуется) или APK
3. Создайте новый keystore или выберите существующий
4. Заполните данные keystore
5. Build Variant: release
6. Signature Versions: V1 и V2
7. Finish

### 3. Настройка ProGuard (опционально)

Откройте `app/proguard-rules.pro` и добавьте правила для библиотек.

### 4. Сборка через командную строку

```bash
./gradlew assembleRelease
```

APK будет в `app/build/outputs/apk/release/app-release.apk`

Для AAB (Google Play):

```bash
./gradlew bundleRelease
```

AAB будет в `app/build/outputs/bundle/release/app-release.aab`

---

## 📦 Публикация в Google Play

### 1. Подготовка

- Создайте Google Play Developer аккаунт ($25 одноразово)
- Подготовьте графические материалы:
  - Иконка приложения: 512x512 PNG
  - Feature Graphic: 1024x500 PNG
  - Скриншоты: минимум 2 (Phone, 7-inch Tablet)
  - Промо видео (опционально): YouTube ссылка

### 2. Создание приложения

1. [Google Play Console](https://play.google.com/console)
2. Create app
3. Заполните информацию:
   - Название: БомбаДня
   - Язык: Русский
   - Категория: Social
   - Тип: App

### 3. Загрузка AAB

1. Production → Create new release
2. Upload AAB файл
3. Заполните Release notes
4. Review release → Start rollout to Production

### 4. Политика конфиденциальности

Создайте страницу с политикой конфиденциальности (обязательно для приложений с пользовательским контентом).

Шаблон находится в файле `PRIVACY_POLICY.md` (создайте его).

---

## 🔐 Безопасность

### Рекомендации:

1. **Никогда не коммитьте `google-services.json`** в публичный репозиторий
2. Используйте `.gitignore` для чувствительных файлов
3. Храните API ключи в `local.properties` или Firebase Remote Config
4. Включите ProGuard для release сборок
5. Регулярно обновляйте зависимости

### Защита от спама:

1. Ограничение: 1 бомба на пользователя за сессию
2. Rate limiting в Firebase Rules
3. Модерация контента (через Cloud Functions + ML Kit для обнаружения неподобающего контента)

---

## 📊 Мониторинг и аналитика

### Firebase Analytics

Отслеживайте ключевые события:

```kotlin
Firebase.analytics.logEvent("bomb_uploaded") {
    param("session_id", sessionId)
    param("has_blur", isBlurred)
    param("media_type", if (isVideo) "video" else "image")
}

Firebase.analytics.logEvent("vote_cast") {
    param("emoji", emoji)
    param("bomb_id", bombId)
}
```

### Crashlytics (рекомендуется)

Добавьте в `app/build.gradle.kts`:

```kotlin
plugins {
    id("com.google.firebase.crashlytics") version "2.9.9"
}

dependencies {
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
```

---

## 🤝 Вклад в проект

### Как внести изменения:

1. Fork репозиторий
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Создайте Pull Request

### Code Style:

- Следуйте [Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)
- Используйте Android Studio автоформатирование (Ctrl+Alt+L)
- Комментируйте сложную логику
- Пишите понятные commit messages

---

## 📝 Лицензия

MIT License - см. [LICENSE](LICENSE) файл для деталей.

---

## 💬 Контакты и поддержка

- **Issues:** [GitHub Issues](https://github.com/yourusername/bombaday/issues)
- **Email:** support@bombaday.app
- **Telegram:** @bombaday_support

---

## 📚 Дополнительные ресурсы

### Документация:

- [Android Developers](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase для Android](https://firebase.google.com/docs/android/setup)
- [CameraX](https://developer.android.com/training/camerax)
- [Material Design 3](https://m3.material.io/)

### Полезные ссылки:

- [Android Weekly Newsletter](https://androidweekly.net/)
- [Kotlin Weekly](https://kotlinweekly.net/)
- [ProAndroidDev](https://proandroiddev.com/)

---

## 🎉 Roadmap (будущие возможности)

- [ ] CameraX интеграция для съемки в реальном времени
- [ ] ML Kit для автоматического распознавания лиц и блюра
- [ ] Стикеры и фильтры в реальном времени
- [ ] Чаты между друзьями
- [ ] Групповые "бомба-челленджи"
- [ ] Интеграция с Instagram/TikTok для шэринга
- [ ] Темная/светлая тема (сейчас только темная)
- [ ] Локализация (английский, испанский и др.)
- [ ] Платежи через Google Play Billing (PRO подписка)
- [ ] AR-эффекты для фото/видео

---

**Разработано с 💣 и 🔥 для БомбаДня**

_Версия 1.0.0 - Февраль 2026_
