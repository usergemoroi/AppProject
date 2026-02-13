# 🗺️ БомбаДня - Карта проекта

Визуальный гид по всей структуре проекта.

---

## 📚 Документация (Начни отсюда!)

```
📖 Документация
├── 🏁 QUICKSTART.md         ← Начни здесь! (5 минут)
├── 📘 README.md             ← Полная документация (EN)
├── 📗 README_RU.md          ← Полная документация (RU)
├── 🔧 ИНСТРУКЦИЯ.md         ← Где что менять (RU) ⭐
├── 💡 EXAMPLES.md           ← Примеры кода
├── ✅ TODO.md               ← Что делать дальше
├── 📋 PROJECT_SUMMARY.md    ← Краткое описание
├── 📁 FILES_LIST.md         ← Список всех файлов
├── 📝 CHANGELOG.md          ← История изменений
└── 🗺️ PROJECT_MAP.md        ← Этот файл
```

**Рекомендуемый порядок чтения:**
1. ⚡ [QUICKSTART.md](QUICKSTART.md) - быстрый старт
2. 🔧 [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) - где что менять
3. 💡 [EXAMPLES.md](EXAMPLES.md) - примеры кода
4. 📖 [README.md](README.md) - детали

---

## 🏗️ Структура кода

```
📱 Android App
├── 🎨 UI Layer
│   ├── screens/              ← Экраны (Feed, Camera, Profile)
│   ├── components/           ← UI компоненты (BombCard)
│   └── theme/                ← Цвета, шрифты, темы
│
├── 🧠 Business Logic
│   ├── viewmodel/            ← MainViewModel (вся логика)
│   ├── model/                ← Модели данных (Bomb, User)
│   └── repository/           ← Работа с данными (Firebase)
│
├── 🔧 Services & Utils
│   ├── service/              ← Push-уведомления (FCM)
│   └── util/                 ← Константы, helpers
│
├── ⚡ Native Code
│   └── cpp/                  ← C++ оптимизации
│
└── 🎨 Resources
    └── res/                  ← Иконки, строки, XML
```

---

## 📂 Детальная структура файлов

### 🎯 Главные файлы приложения

```
MainActivity.kt               ← Главная Activity + навигация
BombaDayApplication.kt        ← Application класс + инициализация
```

**Что делают:**
- MainActivity управляет навигацией между экранами
- BombaDayApplication инициализирует Firebase и уведомления

---

### 🎨 UI Экраны (app/.../ui/screens/)

```
FeedScreen.kt                 ← Лента бомб + топ-10
   ├── Топ-10 фильтр
   ├── Все бомбы фильтр
   ├── Сетка карточек (2 колонки)
   └── Голосование эмодзи

CameraScreen.kt               ← Загрузка контента
   ├── Таймер обратного отсчета
   ├── Выбор из галереи
   ├── Превью фото
   ├── Переключатель блюра
   └── Отправка бомбы

ProfileScreen.kt              ← Профиль пользователя
   ├── Статистика (стрик, монеты)
   ├── Достижения
   ├── PRO апгрейд
   └── Настройки
```

**Где менять:**
- Цвета → `ui/theme/Color.kt`
- Шрифты → `ui/theme/Type.kt`
- Сетку → `FeedScreen.kt` (GridCells.Fixed(2))

---

### 🧩 UI Компоненты (app/.../ui/components/)

```
BombCard.kt                   ← Карточка бомбы
   ├── Изображение/видео
   ├── Позиция в топе (#1, #2...)
   ├── Эмодзи-кнопки (🔥💀🤡)
   └── Счетчик голосов
```

**Как использовать:**
```kotlin
BombCard(
    bomb = bomb,
    position = 1,
    selectedEmoji = "🔥",
    onEmojiClick = { emoji -> viewModel.voteBomb(bombId, emoji) },
    onBombClick = { /* открыть детали */ }
)
```

---

### 📦 Модели данных (app/.../model/)

```
Bomb.kt
   ├── data class Bomb        ← Основная модель бомбы
   ├── data class BombSession ← Сессия дня
   ├── data class UserProfile ← Профиль пользователя
   ├── data class Vote        ← Голос
   └── enum classes           ← MediaType, SessionStatus и др.
```

**Важные поля Bomb:**
- `id` - уникальный ID
- `mediaUrl` - ссылка на фото/видео
- `reactions` - Map эмодзи → количество
- `totalVotes` - общее количество голосов
- `position` - место в топе

---

### 🗄️ Репозитории (app/.../repository/)

```
BombRepository.kt             ← Работа с бомбами
   ├── uploadBomb()           ← Загрузка в Firebase Storage
   ├── getTodaysBombs()       ← Получить все бомбы дня
   ├── getTopBombs()          ← Топ-10
   ├── voteBomb()             ← Проголосовать эмодзи
   └── observeTopBombs()      ← Реалтайм обновление

UserRepository.kt             ← Работа с пользователями
   ├── getUserProfile()       ← Получить профиль
   ├── updateStreak()         ← Обновить стрик
   ├── addCoins()             ← Добавить монеты
   └── unlockAchievement()    ← Разблокировать достижение
```

**Firebase коллекции:**
- `bombs` - все бомбы
- `bomb_sessions` - сессии дня
- `users` - профили пользователей
- `votes` - голоса

---

### 🧠 ViewModel (app/.../viewmodel/)

```
MainViewModel.kt              ← Главная бизнес-логика
   ├── StateFlows
   │   ├── currentSession     ← Текущая сессия
   │   ├── topBombs           ← Топ-10 бомб
   │   ├── userProfile        ← Профиль
   │   └── timeRemaining      ← Таймер
   │
   └── Functions
       ├── loadCurrentSession()
       ├── uploadBomb()
       ├── voteBomb()
       └── refreshData()
```

**Как использовать:**
```kotlin
val viewModel: MainViewModel = viewModel()
val topBombs by viewModel.topBombs.collectAsState()

// В UI
topBombs.forEach { bomb ->
    BombCard(bomb = bomb, ...)
}
```

---

### 🔔 Сервисы (app/.../service/)

```
BombaPushService.kt           ← FCM Push-уведомления
   ├── onMessageReceived()    ← Обработка пушей
   │   ├── "bomb_launch"      → Запущена бомба
   │   └── "voting_end"       → Результаты
   │
   └── onNewToken()           ← Новый FCM токен
```

**Типы уведомлений:**
1. Бомба запущена: "💣 БОМБА ЗАПУЩЕНА! 60 секунд"
2. Результаты: "🏆 ТЫ В ТОП-10!" с позицией

---

### 🛠️ Утилиты (app/.../util/)

```
Constants.kt                  ← Все константы
   ├── Timing
   │   ├── CAPTURE_WINDOW_SECONDS = 60
   │   └── VOTING_WINDOW_MINUTES = 5
   ├── Firebase Collections
   │   ├── COLLECTION_BOMBS
   │   └── COLLECTION_USERS
   └── Emojis
       └── AVAILABLE_EMOJIS = ["🔥", "💀", "🤡"]

NativeLib.kt                  ← JNI интерфейс для C++
VideoCompressor.kt            ← Компрессия через C++
```

**Что менять:**
- Время окна → `CAPTURE_WINDOW_SECONDS`
- Время голосования → `VOTING_WINDOW_MINUTES`
- Эмодзи → `AVAILABLE_EMOJIS`

---

### ⚡ C++ Native (app/src/main/cpp/)

```
native-lib.cpp                ← Основной код
video_compressor.cpp          ← Компрессия изображений
   ├── compressImageNative()  ← Сжатие bitmap
   └── applyBlurNative()      ← Размытие

CMakeLists.txt                ← CMake конфигурация
```

**Зачем C++:**
- Быстрая компрессия больших файлов
- Оптимизация размытия (blur)
- Меньший размер APK

---

### 🎨 Ресурсы (app/src/main/res/)

```
res/
├── drawable/                 ← Векторные иконки
│   ├── ic_bomb.xml
│   ├── ic_fire.xml
│   ├── bg_button_primary.xml
│   └── bg_card.xml
│
├── mipmap-*/                 ← Иконки приложения
│   ├── ic_launcher.png       (ЗАМЕНИТЬ на реальные!)
│   └── ic_launcher_round.png
│
├── values/
│   ├── strings.xml           ← Все тексты (RU)
│   ├── colors.xml            ← Цвета
│   └── themes.xml            ← Темы
│
└── xml/
    ├── file_paths.xml        ← FileProvider
    ├── backup_rules.xml
    └── data_extraction_rules.xml
```

**Где менять тексты:**
```xml
<!-- res/values/strings.xml -->
<string name="app_name">БомбаДня</string>
<string name="bomb_launched">💣 БОМБА ЗАПУЩЕНА!</string>
```

---

## 🔥 Firebase Backend

```
Firebase
├── 🗄️ Firestore Database
│   ├── bombs/                ← Все бомбы
│   ├── bomb_sessions/        ← Сессии дня
│   ├── users/                ← Профили
│   └── votes/                ← Голоса
│
├── 📦 Storage
│   ├── bombs/
│   │   └── {sessionId}/
│   │       └── {bombId}.jpg
│   └── thumbnails/
│
├── 🔔 Cloud Messaging
│   └── Topic: all_users
│
└── ⚡ Cloud Functions
    ├── scheduleDailyBomb     ← Запуск бомбы
    ├── endVotingSession      ← Конец голосования
    └── awardTopBombers       ← Начисление монет
```

**Настройка:** См. [README.md](README.md) раздел "Firebase Setup"

---

## 🔄 Как всё работает (Flow)

```
┌─────────────────┐
│  User получает  │
│  PUSH (19:00)   │
└────────┬────────┘
         ↓
┌─────────────────┐
│  Таймер: 60 сек │
│  Снимает фото   │
└────────┬────────┘
         ↓
┌─────────────────┐
│  uploadBomb()   │
│  → Firebase     │
└────────┬────────┘
         ↓
┌─────────────────┐
│  Голосование    │
│  5 минут        │
└────────┬────────┘
         ↓
┌─────────────────┐
│  Топ-10         │
│  определяется   │
└────────┬────────┘
         ↓
┌─────────────────┐
│  Push: результат│
│  + монеты       │
└─────────────────┘
```

**Код по этапам:**
1. Push → `BombaPushService.kt`
2. Таймер → `CameraScreen.kt`
3. Upload → `BombRepository.uploadBomb()`
4. Vote → `BombRepository.voteBomb()`
5. Results → `Cloud Functions`

---

## 🎯 Быстрая навигация по задачам

### Хочу изменить UI

```
Цвета      → ui/theme/Color.kt
Тексты     → res/values/strings.xml
Шрифты     → ui/theme/Type.kt
Карточки   → ui/components/BombCard.kt
Экраны     → ui/screens/
```

### Хочу изменить логику

```
Загрузка   → repository/BombRepository.kt
Профиль    → repository/UserRepository.kt
Таймер     → viewmodel/MainViewModel.kt
Константы  → util/Constants.kt
```

### Хочу изменить Firebase

```
Конфиг     → app/google-services.json
Rules      → Firebase Console
Functions  → firebase-functions-example.js
```

### Хочу добавить фичу

```
1. Модель      → model/Bomb.kt (если нужно)
2. Repository  → repository/
3. ViewModel   → viewmodel/MainViewModel.kt
4. UI          → ui/screens/ или ui/components/
5. Тесты       → (TODO)
```

---

## 📚 Полезные ссылки внутри проекта

### Для начинающих
- [QUICKSTART.md](QUICKSTART.md) - запуск за 5 минут
- [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) - где что менять

### Для разработчиков
- [EXAMPLES.md](EXAMPLES.md) - примеры кода
- [TODO.md](TODO.md) - что делать
- [FILES_LIST.md](FILES_LIST.md) - список файлов

### Для понимания проекта
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - краткое описание
- [CHANGELOG.md](CHANGELOG.md) - что изменилось
- [README.md](README.md) - полная документация

---

## 🔍 Поиск по проекту

### По функциональности

| Что искать | Где искать |
|------------|------------|
| Загрузка бомбы | `BombRepository.uploadBomb()` |
| Голосование | `BombRepository.voteBomb()` |
| Таймер | `MainViewModel.startTimerUpdates()` |
| Push-уведомления | `BombaPushService.kt` |
| Профиль | `UserRepository.kt` + `ProfileScreen.kt` |
| Топ-10 | `BombRepository.getTopBombs()` |
| Эмодзи | `Constants.AVAILABLE_EMOJIS` |
| Стрики | `UserRepository.updateStreak()` |
| Достижения | `UserRepository.unlockAchievement()` |

### По технологии

| Технология | Файлы |
|------------|-------|
| Jetpack Compose | `ui/**/*.kt` |
| Firebase | `repository/*.kt` |
| CameraX | `CameraScreen.kt` (TODO) |
| C++ | `cpp/*.cpp` |
| Navigation | `MainActivity.kt` |

---

## 🚀 Дальнейшие шаги

1. ✅ **Изучил карту** → Переходи к [QUICKSTART.md](QUICKSTART.md)
2. ✅ **Запустил проект** → Читай [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md)
3. ✅ **Хочешь кастомизировать** → Смотри [EXAMPLES.md](EXAMPLES.md)
4. ✅ **Готов к деплою** → Проверь [TODO.md](TODO.md)

---

**Карта проекта v1.0**  
_Последнее обновление: 13 февраля 2026_
