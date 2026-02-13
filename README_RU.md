# 💣 БомбаДня - Социальное Android приложение

<div align="center">

![БомбаДня Logo](https://via.placeholder.com/200x200/FF6B35/FFFFFF?text=💣)

**Каждый день 60 секунд на трэш-контент!**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Android-7.0+-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/Firebase-Ready-orange.svg)](https://firebase.google.com)

[English](README.md) | **Русский**

</div>

---

## 🎯 О приложении

**БомбаДня** - это приложение, где каждый день в случайное время все пользователи одновременно получают **ровно 60 секунд** для загрузки самого трэшового контента. После этого 5-минутное голосование эмодзи определяет топ-10 бомб дня!

### ✨ Ключевые фичи

- ⏱️ **60-секундное окно** - успей загрузить фото/видео
- 🔥💀🤡 **Голосование эмодзи** - три реакции
- 🏆 **Топ-10 дня** - реалтайм обновление
- 🎭 **Анонимность** - размытие лиц опционально
- 📈 **Стрики** - бонусы за ежедневное участие
- 💰 **Виртуальные монеты** - за достижения
- 🗺️ **Гео-фильтры** - рядом/город/страна/глобал
- 💎 **PRO версия** - без рекламы, бонусы

---

## 🚀 Быстрый старт

### 1️⃣ Установка (5 минут)

```bash
# 1. Клонируйте репозиторий
git clone https://github.com/yourusername/bombaday.git
cd bombaday

# 2. Откройте в Android Studio
# File → Open → выберите папку bombaday

# 3. Подождите синхронизации Gradle

# 4. Нажмите Run (▶️) или Shift+F10
```

### 2️⃣ Первый запуск

Приложение сразу работает с демо Firebase конфигом!

Для продакшена нужно:
1. Создать Firebase проект
2. Скачать `google-services.json`
3. Заменить файл в `app/`

**Подробнее:** [QUICKSTART.md](QUICKSTART.md)

---

## 📱 Скриншоты

```
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│                 │  │                 │  │                 │
│   🔥 ТОП-10    │  │   📸 КАМЕРА    │  │  👤 ПРОФИЛЬ    │
│                 │  │                 │  │                 │
│  [Бомба 1]     │  │   60 секунд     │  │  🔥 Стрик: 7   │
│  [Бомба 2]     │  │                 │  │  💰 Монеты: 150│
│  [Бомба 3]     │  │  [СНЯТЬ ФОТО]  │  │  💣 Бомб: 25   │
│  ...           │  │  [ИЗ ГАЛЕРЕИ]  │  │  🏆 Топ-10: 3  │
│                 │  │                 │  │                 │
└─────────────────┘  └─────────────────┘  └─────────────────┘
   Лента бомб         Загрузка            Статистика
```

---

## 🛠️ Технологии

- **Kotlin** 1.9.20
- **Jetpack Compose** - UI
- **Firebase** (Firestore, Storage, FCM)
- **CameraX** - камера
- **Media3** - видео
- **C++ NDK** - оптимизации
- **Material Design 3**

**Поддержка:** Android 7.0+ (API 24+)

---

## 📖 Документация

| Файл | Описание |
|------|----------|
| [README.md](README.md) | Полная документация (EN) |
| [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) | Где что менять (RU) 🔥 |
| [QUICKSTART.md](QUICKSTART.md) | Быстрый старт за 5 минут |
| [EXAMPLES.md](EXAMPLES.md) | Примеры кода |
| [TODO.md](TODO.md) | Список задач |
| [CHANGELOG.md](CHANGELOG.md) | История изменений |

---

## 🎨 Кастомизация

### Изменить цвета

```kotlin
// app/src/main/java/.../ui/theme/Color.kt
val Primary = Color(0xFFFF6B35)  // Оранжевый
val Accent = Color(0xFFFFD23F)   // Желтый
```

### Изменить время окна

```kotlin
// app/src/main/java/.../util/Constants.kt
const val CAPTURE_WINDOW_SECONDS = 60  // Измените на 30, 90, 120...
const val VOTING_WINDOW_MINUTES = 5    // Измените на 3, 10, 15...
```

### Добавить новые эмодзи

```kotlin
// В Constants.kt
const val EMOJI_HEART = "❤️"
val AVAILABLE_EMOJIS = listOf("🔥", "💀", "🤡", "❤️")
```

**Больше примеров:** [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md)

---

## 🔧 Настройка Firebase

### Обязательные сервисы:

1. **Firestore Database**
   - Режим: Production
   - Регион: europe-west3
   - Коллекции: bombs, bomb_sessions, users, votes

2. **Firebase Storage**
   - Для хранения фото/видео
   - Лимит: 50 МБ на файл

3. **Cloud Messaging**
   - Для пуш-уведомлений
   - Топик: `all_users`

4. **Authentication**
   - Anonymous mode
   - Для уникальных ID

### Правила безопасности

См. подробные правила в [README.md](README.md), раздел "Firebase Rules"

### Cloud Functions

```bash
# Установка
npm install -g firebase-tools
firebase login
firebase init functions

# Используйте файл firebase-functions-example.js
cp firebase-functions-example.js functions/index.js

# Деплой
cd functions && npm install
firebase deploy --only functions
```

---

## 🏗️ Структура проекта

```
BombaDay/
├── app/src/main/
│   ├── java/com/bombaday/app/
│   │   ├── model/          # Модели данных
│   │   ├── repository/     # Firebase
│   │   ├── viewmodel/      # Логика
│   │   ├── ui/             # UI компоненты
│   │   ├── service/        # Push
│   │   └── util/           # Утилиты
│   ├── cpp/                # C++ оптимизации
│   └── res/                # Ресурсы
├── Документация (MD файлы)
└── Firebase (функции)
```

**Полный список:** [FILES_LIST.md](FILES_LIST.md)

---

## ✅ Что реализовано

### Готово ✓
- [x] UI всех экранов
- [x] Firebase интеграция
- [x] Загрузка фото из галереи
- [x] Голосование эмодзи
- [x] Топ-10 реалтайм
- [x] Профиль и достижения
- [x] Стрики и монеты
- [x] Push-уведомления
- [x] C++ компрессия
- [x] Темная тема

### В разработке ⚠️
- [ ] CameraX live preview (только галерея)
- [ ] ML Kit для лиц (manual blur)
- [ ] Google Play Billing (UI готов)

**Полный список:** [TODO.md](TODO.md)

---

## 📊 Roadmap

### v1.1 (1 месяц)
- CameraX live camera
- ML Kit face detection
- Google Play Billing
- Больше достижений

### v1.2 (2 месяца)
- Стикеры и фильтры
- Локализация (EN, ES)
- Светлая тема

### v2.0 (3-6 месяцев)
- Чаты между друзьями
- AR-эффекты
- Групповые челленджи
- Social sharing

---

## 🤝 Вклад в проект

Приветствуются:
- 🐛 Баг репорты (GitHub Issues)
- 💡 Предложения новых фич
- 🔧 Pull Requests
- 📝 Улучшения документации
- 🌍 Переводы

---

## 📝 Лицензия

MIT License - см. [LICENSE](LICENSE)

---

## 💬 Контакты

- **GitHub Issues:** [Issues](https://github.com/yourusername/bombaday/issues)
- **Email:** support@bombaday.app
- **Telegram:** @bombaday_support

---

## 🎉 Благодарности

Создано с использованием:
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase](https://firebase.google.com/)
- [CameraX](https://developer.android.com/training/camerax)
- [Material Design](https://m3.material.io/)

---

## 🔥 Quick Links

- 📚 [Полная документация](README.md)
- 🚀 [Быстрый старт](QUICKSTART.md)
- 🔧 [Где что менять](ИНСТРУКЦИЯ.md)
- 💻 [Примеры кода](EXAMPLES.md)
- ✅ [TODO список](TODO.md)
- 📋 [Список файлов](FILES_LIST.md)

---

<div align="center">

**Разработано с 💣 и 🔥 для БомбаДня**

_Версия 1.0.0 | Февраль 2026_

[![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-blue.svg)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/Powered%20by-Firebase-orange.svg)](https://firebase.google.com)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)

</div>
