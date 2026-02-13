# 📁 Полный список файлов проекта БомбаДня

## 📋 Корневые файлы

```
.gitignore                    - Игнорируемые файлы для Git
README.md                     - Главная документация (EN/RU)
ИНСТРУКЦИЯ.md                 - Подробная инструкция где что менять (RU)
QUICKSTART.md                 - Быстрый старт за 5 минут
EXAMPLES.md                   - Примеры кода и использования
CHANGELOG.md                  - История изменений
LICENSE                       - Лицензия MIT
PROJECT_SUMMARY.md            - Краткое описание проекта
FILES_LIST.md                 - Этот файл
firebase-functions-example.js - Пример Cloud Functions для Firebase
create_icons.sh               - Скрипт для создания иконок
```

## 🔧 Gradle конфигурация

```
build.gradle.kts              - Корневой Gradle файл
settings.gradle.kts           - Настройки модулей
gradle.properties             - Свойства Gradle
gradlew                       - Gradle Wrapper (Linux/Mac)
gradle/wrapper/
  └── gradle-wrapper.properties - Конфигурация Wrapper
```

## 📱 Модуль app/

### Gradle

```
app/build.gradle.kts          - Конфигурация приложения
app/proguard-rules.pro        - Правила обфускации
app/google-services.json      - Firebase конфигурация (ЗАМЕНИТЬ!)
app/google-services.json.example - Пример конфигурации
```

### Android Manifest

```
app/src/main/AndroidManifest.xml - Манифест приложения
```

### Kotlin код

#### Модели данных (model/)
```
app/src/main/java/com/bombaday/app/model/
  └── Bomb.kt                 - Модели: Bomb, BombSession, UserProfile, Vote
```

#### Репозитории (repository/)
```
app/src/main/java/com/bombaday/app/repository/
  ├── BombRepository.kt       - Работа с бомбами в Firebase
  └── UserRepository.kt       - Работа с пользователями
```

#### ViewModel (viewmodel/)
```
app/src/main/java/com/bombaday/app/viewmodel/
  └── MainViewModel.kt        - Главная бизнес-логика
```

#### UI - Тема (ui/theme/)
```
app/src/main/java/com/bombaday/app/ui/theme/
  ├── Color.kt                - Цветовая палитра
  ├── Theme.kt                - Тема приложения
  └── Type.kt                 - Типография (шрифты)
```

#### UI - Компоненты (ui/components/)
```
app/src/main/java/com/bombaday/app/ui/components/
  └── BombCard.kt             - Карточка бомбы + EmojiButton
```

#### UI - Экраны (ui/screens/)
```
app/src/main/java/com/bombaday/app/ui/screens/
  ├── FeedScreen.kt           - Лента бомб
  ├── CameraScreen.kt         - Экран камеры/загрузки
  └── ProfileScreen.kt        - Профиль пользователя
```

#### Сервисы (service/)
```
app/src/main/java/com/bombaday/app/service/
  └── BombaPushService.kt     - Firebase Cloud Messaging
```

#### Утилиты (util/)
```
app/src/main/java/com/bombaday/app/util/
  ├── Constants.kt            - Все константы приложения
  ├── NativeLib.kt            - JNI интерфейс для C++
  └── VideoCompressor.kt      - Компрессия через C++
```

#### Главные классы
```
app/src/main/java/com/bombaday/app/
  ├── MainActivity.kt         - Главная Activity
  └── BombaDayApplication.kt  - Application класс
```

### C++ Native код

```
app/src/main/cpp/
  ├── CMakeLists.txt          - CMake конфигурация
  ├── native-lib.cpp          - Основной native код
  └── video_compressor.cpp    - Компрессия изображений/видео
```

### XML Ресурсы

#### Строки и значения (values/)
```
app/src/main/res/values/
  ├── strings.xml             - Все текстовые строки (RU)
  ├── colors.xml              - Цветовая палитра
  └── themes.xml              - Тема Material 3
```

#### Drawable (векторные иконки)
```
app/src/main/res/drawable/
  ├── bg_button_primary.xml   - Фон кнопки
  ├── bg_card.xml             - Фон карточки
  ├── ic_bomb.xml             - Иконка бомбы
  ├── ic_fire.xml             - Иконка огня
  ├── ic_launcher_background.xml - Фон иконки
  └── ic_launcher_foreground.xml - Передний слой иконки
```

#### Mipmap (иконки приложения)
```
app/src/main/res/mipmap-anydpi-v26/
  ├── ic_launcher.xml         - Адаптивная иконка
  └── ic_launcher_round.xml   - Круглая иконка

app/src/main/res/mipmap-mdpi/
  ├── ic_launcher.png         - 48x48
  └── ic_launcher_round.png

app/src/main/res/mipmap-hdpi/
  ├── ic_launcher.png         - 72x72
  └── ic_launcher_round.png

app/src/main/res/mipmap-xhdpi/
  ├── ic_launcher.png         - 96x96
  └── ic_launcher_round.png

app/src/main/res/mipmap-xxhdpi/
  ├── ic_launcher.png         - 144x144
  └── ic_launcher_round.png

app/src/main/res/mipmap-xxxhdpi/
  ├── ic_launcher.png         - 192x192
  └── ic_launcher_round.png
```

#### XML конфигурация
```
app/src/main/res/xml/
  ├── backup_rules.xml        - Правила бэкапа
  ├── data_extraction_rules.xml - Правила извлечения данных
  └── file_paths.xml          - Пути FileProvider
```

---

## 📊 Статистика

### Файлы по типам:

| Тип файла | Количество | Назначение |
|-----------|------------|------------|
| .kt (Kotlin) | 16 | Логика приложения |
| .cpp (C++) | 3 | Нативные оптимизации |
| .xml (Android) | 20+ | UI ресурсы |
| .md (Markdown) | 8 | Документация |
| .gradle.kts | 3 | Конфигурация сборки |
| .json | 2 | Firebase конфиг |
| .pro | 1 | ProGuard правила |
| .sh | 1 | Скрипты |

**Всего файлов:** ~60+

### Строки кода:

| Язык | Строк | Файлов |
|------|-------|--------|
| Kotlin | ~3500 | 16 |
| C++ | ~500 | 3 |
| XML | ~800 | 20+ |
| JavaScript | ~500 | 1 |
| Markdown | ~2000 | 8 |

**Всего строк кода:** ~7300

---

## 🎯 Где что искать

### Хочу изменить цвета → `app/src/main/java/.../ui/theme/Color.kt`
### Хочу изменить тексты → `app/src/main/res/values/strings.xml`
### Хочу изменить логику бомб → `app/src/main/java/.../repository/BombRepository.kt`
### Хочу изменить UI экрана → `app/src/main/java/.../ui/screens/`
### Хочу изменить константы → `app/src/main/java/.../util/Constants.kt`
### Хочу настроить Firebase → `app/google-services.json`
### Хочу изменить иконку → `app/src/main/res/mipmap-*/`
### Хочу настроить сборку → `app/build.gradle.kts`
### Хочу добавить функцию → `firebase-functions-example.js`

---

## 🚀 Что нужно обязательно заменить

Перед продакшеном замените:

1. ✅ `app/google-services.json` - Firebase конфиг
2. ✅ Все иконки в `mipmap-*/` - на реальные PNG
3. ✅ Package name в `build.gradle.kts` (если нужно)
4. ✅ Strings в `strings.xml` (кастомизация)
5. ✅ Firebase Rules (см. README.md)
6. ✅ Cloud Functions (деплой)

---

## 📝 Примечания

- Все `.png` файлы в `mipmap-*/` сейчас пустые (placeholder)
- `google-services.json` содержит демо конфигурацию
- Некоторые TODO оставлены в коде для будущих улучшений
- C++ библиотека компилируется автоматически через CMake

---

_Этот список актуален для версии 1.0.0_
