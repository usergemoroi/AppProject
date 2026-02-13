# 👋 Добро пожаловать в БомбаДня!

<div align="center">

```
██████╗  ██████╗ ███╗   ███╗██████╗  █████╗ ██████╗  █████╗ ██╗   ██╗
██╔══██╗██╔═══██╗████╗ ████║██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
██████╔╝██║   ██║██╔████╔██║██████╔╝███████║██║  ██║███████║ ╚████╔╝ 
██╔══██╗██║   ██║██║╚██╔╝██║██╔══██╗██╔══██║██║  ██║██╔══██║  ╚██╔╝  
██████╔╝╚██████╔╝██║ ╚═╝ ██║██████╔╝██║  ██║██████╔╝██║  ██║   ██║   
╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝   ╚═╝   
```

**💣 Каждый день 60 секунд на трэш-контент! 🔥**

[🚀 Быстрый старт](#-как-начать-3-шага) • [📚 Документация](#-документация) • [💻 Код](#-код-проекта)

</div>

---

## 🎯 Что это?

**БомбаДня** - это Android приложение, где:

- 📱 Каждый день в случайное время все получают **пуш**
- ⏱️ У всех есть **ровно 60 секунд** загрузить фото/видео
- 🔥💀🤡 Потом **5 минут голосования** эмодзи
- 🏆 **Топ-10 бомб дня** определяется реалтайм
- 🎭 Всё **анонимно**, с размытием лиц
- 📈 **Стрики**, монеты, достижения
- 💎 **PRO версия** без рекламы

---

## ⚡ Как начать (3 шага)

### 1️⃣ Установите Android Studio

Скачайте: https://developer.android.com/studio

### 2️⃣ Откройте проект

```bash
git clone https://github.com/yourusername/bombaday.git
cd bombaday
# Откройте в Android Studio: File → Open → bombaday
```

### 3️⃣ Запустите

Нажмите **▶️ Run** (или **Shift+F10**)

**Готово!** Приложение запустится с демо конфигом.

📖 **Подробнее:** [QUICKSTART.md](QUICKSTART.md) (5 минут)

---

## 📚 Документация

### 🟢 Для начинающих

| Документ | Время | Что внутри |
|----------|-------|------------|
| ► [QUICKSTART.md](QUICKSTART.md) | 5 мин | Быстрый запуск проекта |
| ► [README_RU.md](README_RU.md) | 20 мин | Краткая документация (RU) |
| ► [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) ⭐ | 30 мин | Где что менять (подробно!) |

### 🟡 Для разработчиков

| Документ | Что внутри |
|----------|------------|
| ► [EXAMPLES.md](EXAMPLES.md) | Примеры кода для разных задач |
| ► [PROJECT_MAP.md](PROJECT_MAP.md) | Визуальная карта проекта |
| ► [FILES_LIST.md](FILES_LIST.md) | Полный список всех файлов |
| ► [TODO.md](TODO.md) | Что нужно сделать |

### 🔵 Справочники

| Документ | Что внутри |
|----------|------------|
| ► [README.md](README.md) | Полная документация (EN) |
| ► [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) | Краткое описание проекта |
| ► [INDEX.md](INDEX.md) | Навигация по документации |
| ► [CHANGELOG.md](CHANGELOG.md) | История изменений |

---

## 💻 Код проекта

### 📱 Android App (Kotlin)

```
app/src/main/java/com/bombaday/app/
├── 🎨 ui/                   ← UI (Jetpack Compose)
│   ├── screens/            (FeedScreen, CameraScreen, ProfileScreen)
│   ├── components/         (BombCard, EmojiButton)
│   └── theme/              (Colors, Theme, Typography)
│
├── 🧠 viewmodel/            ← Бизнес-логика
│   └── MainViewModel.kt
│
├── 🗄️ repository/           ← Firebase
│   ├── BombRepository.kt   (работа с бомбами)
│   └── UserRepository.kt   (работа с профилями)
│
├── 📦 model/                ← Модели данных
│   └── Bomb.kt
│
├── 🔔 service/              ← Push-уведомления
│   └── BombaPushService.kt
│
└── 🛠️ util/                 ← Утилиты
    ├── Constants.kt        (все константы)
    ├── NativeLib.kt        (JNI для C++)
    └── VideoCompressor.kt  (компрессия)
```

**Всего:** 17 Kotlin файлов (~3500 строк)

### ⚡ Native (C++)

```
app/src/main/cpp/
├── native-lib.cpp          ← Основной код
├── video_compressor.cpp    ← Компрессия изображений
└── CMakeLists.txt          ← CMake конфигурация
```

**Всего:** 2 C++ файла (~500 строк)

### 🎨 Resources (XML)

```
app/src/main/res/
├── drawable/               ← Векторные иконки
├── mipmap-*/               ← Иконки приложения
├── values/
│   ├── strings.xml         (все тексты)
│   ├── colors.xml          (цвета)
│   └── themes.xml          (темы)
└── xml/                    ← Конфигурация
```

**Всего:** 14 XML файлов

---

## 🎯 Быстрые задачи

### "Хочу изменить цвета"

```kotlin
// app/src/.../ui/theme/Color.kt
val Primary = Color(0xFFFF6B35)  // ← Измени здесь
val Accent = Color(0xFFFFD23F)
```

📖 См. [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) → раздел "Дизайн"

### "Хочу изменить тексты"

```xml
<!-- app/src/main/res/values/strings.xml -->
<string name="app_name">БомбаДня</string>
<string name="bomb_launched">💣 БОМБА ЗАПУЩЕНА!</string>
```

📖 См. [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) → раздел "Дизайн"

### "Хочу изменить время окна"

```kotlin
// app/src/.../util/Constants.kt
const val CAPTURE_WINDOW_SECONDS = 60    // ← Измени здесь
const val VOTING_WINDOW_MINUTES = 5
```

📖 См. [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) → раздел "Константы"

### "Хочу добавить новые эмодзи"

```kotlin
// Constants.kt
const val EMOJI_HEART = "❤️"
val AVAILABLE_EMOJIS = listOf("🔥", "💀", "🤡", "❤️")
```

📖 См. [EXAMPLES.md](EXAMPLES.md) → раздел "Примеры"

### "Хочу настроить Firebase"

1. Создайте проект: https://console.firebase.google.com/
2. Скачайте `google-services.json`
3. Замените файл в `app/`

📖 См. [README.md](README.md) → раздел "Firebase Setup"

---

## 🔥 Технологии

- **Kotlin** 1.9.20
- **Jetpack Compose** - современный UI
- **Firebase** (Firestore, Storage, FCM, Analytics)
- **CameraX** - камера
- **Media3** - видео
- **C++ NDK** - оптимизации
- **Material Design 3** - дизайн
- **Coroutines** - асинхронность

**Поддержка:** Android 7.0 - 14 (API 24-34)

---

## ✅ Статус проекта

### Готово ✓

- [x] 🎨 UI всех экранов (Feed, Camera, Profile)
- [x] 🗄️ Firebase интеграция полная
- [x] 📸 Загрузка фото из галереи
- [x] 🔥 Система голосования эмодзи
- [x] 🏆 Топ-10 реалтайм обновление
- [x] 👤 Профиль с достижениями
- [x] 📈 Стрики и монеты
- [x] 🔔 Push-уведомления
- [x] ⚡ C++ оптимизации
- [x] 🎭 Размытие (manual)
- [x] 📱 Android 7-14 support

### В разработке ⚠️

- [ ] 📷 CameraX live preview (сейчас только галерея)
- [ ] 🤖 ML Kit для автоопределения лиц
- [ ] 💳 Google Play Billing (UI готов)

### Планируется 🔜

- [ ] Стикеры и AR-фильтры
- [ ] Чаты между друзьями
- [ ] Локализация (EN, ES)
- [ ] Светлая тема

📋 **Полный список:** [TODO.md](TODO.md)

---

## 🚀 Дальше

### Я новичок

1. Прочитайте [QUICKSTART.md](QUICKSTART.md) - 5 минут
2. Запустите проект
3. Изучите [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) - где что менять
4. Попробуйте изменить цвета/тексты
5. Смотрите [EXAMPLES.md](EXAMPLES.md) для примеров

### Я опытный разработчик

1. [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) + [PROJECT_MAP.md](PROJECT_MAP.md) - 30 мин
2. Просмотрите код `viewmodel/` и `repository/`
3. Настройте Firebase из [README.md](README.md)
4. Проверьте [TODO.md](TODO.md) и начинайте!

### Хочу внести вклад

1. Fork репозиторий
2. Создайте feature branch
3. Сделайте изменения
4. Отправьте Pull Request

---

## 📊 Статистика

| Метрика | Значение |
|---------|----------|
| 📝 Документация | 11 файлов (~130 KB) |
| 💻 Kotlin код | 17 файлов (~3500 строк) |
| ⚡ C++ код | 2 файла (~500 строк) |
| 🎨 XML ресурсы | 14 файлов (~800 строк) |
| ☁️ Cloud Functions | 1 файл (~500 строк) |
| **Всего** | **~5300 строк кода** |

---

## 💬 Контакты

- **GitHub:** https://github.com/yourusername/bombaday
- **Issues:** https://github.com/yourusername/bombaday/issues
- **Email:** support@bombaday.app
- **Telegram:** @bombaday_support

---

## 📄 Лицензия

MIT License - см. [LICENSE](LICENSE)

Свободное использование, изменение и распространение!

---

## 🎉 Благодарности

Создано с использованием:
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase](https://firebase.google.com/)
- [CameraX](https://developer.android.com/training/camerax)
- [Material Design 3](https://m3.material.io/)
- [Kotlin](https://kotlinlang.org/)

---

<div align="center">

## 🔥 Поехали!

Готовы начать? Выберите свой путь:

**[⚡ Быстрый старт](QUICKSTART.md)** • **[📖 Полная документация](README.md)** • **[🔧 Инструкция](ИНСТРУКЦИЯ.md)**

---

**Разработано с 💣 и 🔥 для БомбаДня**

[![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-blue.svg)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/Powered%20by-Firebase-orange.svg)](https://firebase.google.com)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

_Версия 1.0.0 | Февраль 2026_

</div>
