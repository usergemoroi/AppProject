# 📑 БомбаДня - Индекс документации

Быстрая навигация по всей документации проекта.

---

## 🚀 Начало работы

| Документ | Время чтения | Описание |
|----------|--------------|----------|
| [QUICKSTART.md](QUICKSTART.md) | 5 минут | Быстрый старт проекта |
| [README.md](README.md) | 30 минут | Полная документация (EN) |
| [README_RU.md](README_RU.md) | 20 минут | Краткая документация (RU) |
| [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) | 10 минут | Краткое описание проекта |

**Рекомендация:** Начните с [QUICKSTART.md](QUICKSTART.md)

---

## 🔧 Разработка

| Документ | Назначение |
|----------|------------|
| [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) | Где что менять (подробно, RU) ⭐ |
| [EXAMPLES.md](EXAMPLES.md) | Примеры кода для разных задач |
| [PROJECT_MAP.md](PROJECT_MAP.md) | Визуальная карта проекта |
| [FILES_LIST.md](FILES_LIST.md) | Полный список файлов |
| [TODO.md](TODO.md) | Что нужно сделать |

**Рекомендация:** Читайте [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) для практики

---

## 📚 Справочная информация

| Документ | Содержание |
|----------|------------|
| [CHANGELOG.md](CHANGELOG.md) | История изменений версий |
| [LICENSE](LICENSE) | Лицензия проекта (MIT) |

---

## 🔥 Firebase

| Файл | Назначение |
|------|------------|
| `firebase-functions-example.js` | Пример Cloud Functions |
| `app/google-services.json` | Firebase конфигурация (ЗАМЕНИТЬ!) |

**Документация:** См. README.md раздел "Firebase Setup"

---

## 💻 Исходный код

### Kotlin (17 файлов)

**UI:**
- `ui/screens/FeedScreen.kt` - Лента бомб
- `ui/screens/CameraScreen.kt` - Камера/загрузка
- `ui/screens/ProfileScreen.kt` - Профиль
- `ui/components/BombCard.kt` - Карточка бомбы
- `ui/theme/Color.kt` - Цвета
- `ui/theme/Theme.kt` - Тема
- `ui/theme/Type.kt` - Типография

**Логика:**
- `viewmodel/MainViewModel.kt` - Главная логика
- `model/Bomb.kt` - Модели данных
- `repository/BombRepository.kt` - Работа с бомбами
- `repository/UserRepository.kt` - Работа с пользователями

**Сервисы:**
- `service/BombaPushService.kt` - Push-уведомления
- `util/Constants.kt` - Константы
- `util/NativeLib.kt` - JNI интерфейс
- `util/VideoCompressor.kt` - Компрессия

**Главные:**
- `MainActivity.kt` - Главная Activity
- `BombaDayApplication.kt` - Application класс

### C++ (2 файла)

- `cpp/native-lib.cpp` - Основной native код
- `cpp/video_compressor.cpp` - Компрессия изображений

### XML (20+ файлов)

- `res/values/strings.xml` - Тексты
- `res/values/colors.xml` - Цвета
- `res/values/themes.xml` - Темы
- `res/drawable/*.xml` - Векторные иконки
- `res/mipmap-*/*.png` - Иконки приложения

---

## 🎯 Быстрые ссылки по задачам

### "Хочу быстро запустить"
→ [QUICKSTART.md](QUICKSTART.md)

### "Хочу изменить цвета"
→ [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) раздел "Дизайн"  
→ `app/src/main/java/.../ui/theme/Color.kt`

### "Хочу изменить тексты"
→ [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) раздел "Дизайн"  
→ `app/src/main/res/values/strings.xml`

### "Хочу изменить время окна загрузки"
→ [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) раздел "Константы"  
→ `app/src/main/java/.../util/Constants.kt`

### "Хочу добавить новые эмодзи"
→ [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) раздел "Эмодзи реакции"  
→ [EXAMPLES.md](EXAMPLES.md) раздел "Константы"

### "Хочу изменить логику загрузки"
→ [EXAMPLES.md](EXAMPLES.md) раздел "Работа с данными"  
→ `app/src/main/java/.../repository/BombRepository.kt`

### "Хочу настроить Firebase"
→ [README.md](README.md) раздел "Firebase Setup"  
→ [QUICKSTART.md](QUICKSTART.md) раздел "Firebase"

### "Хочу добавить новую фичу"
→ [EXAMPLES.md](EXAMPLES.md) - примеры кода  
→ [PROJECT_MAP.md](PROJECT_MAP.md) - структура

### "Хочу понять как всё работает"
→ [PROJECT_MAP.md](PROJECT_MAP.md) раздел "Flow"  
→ [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) раздел "Основной флоу"

### "Что делать дальше?"
→ [TODO.md](TODO.md) - полный список задач

---

## 📊 Статистика документации

| Тип | Количество | Общий размер |
|-----|------------|--------------|
| Markdown файлы | 10 | ~120 KB |
| Kotlin файлы | 17 | ~3500 строк |
| C++ файлы | 2 | ~500 строк |
| XML файлы | 20+ | ~800 строк |
| JavaScript | 1 | ~500 строк |

**Общие строки документации:** ~5000+  
**Общие строки кода:** ~5300

---

## 🎓 Рекомендуемый порядок изучения

### Для начинающих:

1. **День 1:** [QUICKSTART.md](QUICKSTART.md) - запуск (30 мин)
2. **День 2:** [README_RU.md](README_RU.md) - обзор (1 час)
3. **День 3:** [ИНСТРУКЦИЯ.md](ИНСТРУКЦИЯ.md) - практика (2 часа)
4. **День 4:** [EXAMPLES.md](EXAMPLES.md) - примеры (2 часа)
5. **День 5:** Свои изменения + [TODO.md](TODO.md)

### Для опытных разработчиков:

1. **30 минут:** [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) + [PROJECT_MAP.md](PROJECT_MAP.md)
2. **1 час:** Просмотр кода `viewmodel/` и `repository/`
3. **30 минут:** Firebase setup из [README.md](README.md)
4. **Работа:** Смотрите [TODO.md](TODO.md) и начинайте!

---

## 🔍 Поиск информации

### По ключевым словам:

| Ищу | Где найти |
|-----|-----------|
| "Firebase" | README.md, QUICKSTART.md |
| "Цвета" | ИНСТРУКЦИЯ.md, Color.kt |
| "Таймер" | MainViewModel.kt, CameraScreen.kt |
| "Эмодзи" | Constants.kt, ИНСТРУКЦИЯ.md |
| "Push" | BombaPushService.kt, README.md |
| "Камера" | CameraScreen.kt, EXAMPLES.md |
| "Топ-10" | BombRepository.kt, FeedScreen.kt |
| "PRO" | ProfileScreen.kt, ИНСТРУКЦИЯ.md |

### По типу информации:

| Нужна | Документ |
|-------|----------|
| Быстрая справка | PROJECT_SUMMARY.md |
| Подробная инструкция | README.md |
| Практические примеры | EXAMPLES.md, ИНСТРУКЦИЯ.md |
| Навигация по коду | PROJECT_MAP.md, FILES_LIST.md |
| История изменений | CHANGELOG.md |
| Планы развития | TODO.md |

---

## 📞 Получение помощи

### Документация не помогла?

1. **GitHub Issues:** Создайте issue с тегом `question`
2. **Email:** support@bombaday.app
3. **Telegram:** @bombaday_support

### Нашли ошибку в документации?

1. Создайте issue с тегом `documentation`
2. Или отправьте Pull Request с исправлением

---

## 🎉 Благодарности

Спасибо что используете БомбаДня!

Если документация помогла - поставьте ⭐ на GitHub!

---

## 📝 О этом индексе

**Версия:** 1.0.0  
**Последнее обновление:** 13 февраля 2026  
**Язык:** Русский  

**Поддерживается в актуальном состоянии:** ✅

---

<div align="center">

**Создано с 💣 и 🔥**

[Начать работу](QUICKSTART.md) • [Документация](README.md) • [Примеры](EXAMPLES.md)

</div>
