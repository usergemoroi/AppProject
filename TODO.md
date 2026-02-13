# ✅ TODO - Список задач БомбаДня

## 🔴 КРИТИЧЕСКИ ВАЖНО (Перед запуском)

- [ ] **Заменить `google-services.json`** на реальный Firebase конфиг
  - Файл: `app/google-services.json`
  - Инструкция: README.md, раздел "Настройка Firebase"

- [ ] **Создать Firebase проект** и настроить сервисы:
  - [ ] Firestore Database
  - [ ] Firebase Storage
  - [ ] Cloud Messaging (FCM)
  - [ ] Authentication (Anonymous)
  - [ ] Analytics

- [ ] **Настроить Firebase Rules** для Firestore и Storage
  - Примеры в README.md

- [ ] **Заменить иконки приложения** на реальные PNG
  - Путь: `app/src/main/res/mipmap-*/`
  - Инструмент: https://romannurik.github.io/AndroidAssetStudio/

- [ ] **Создать и задеплоить Cloud Functions**
  - Файл: `firebase-functions-example.js`
  - Команды:
    ```bash
    firebase init functions
    cd functions
    npm install
    firebase deploy --only functions
    ```

---

## 🟠 ВАЖНО (До первого релиза)

### Функционал

- [ ] **Реализовать CameraX live preview**
  - Сейчас: только выбор из галереи
  - Нужно: прямая съемка с камеры
  - Файл: `app/src/.../ui/screens/CameraScreen.kt`
  - Пример в: `EXAMPLES.md`

- [ ] **Добавить ML Kit для автоопределения лиц**
  - Сейчас: ручное включение блюра
  - Нужно: автоматическое определение и размытие лиц
  - Библиотека: `com.google.mlkit:face-detection`

- [ ] **Интегрировать Google Play Billing**
  - Для PRO подписки (299₽/мес)
  - Файл: `ProfileScreen.kt` → кнопка "Получить PRO"
  - Документация: https://developer.android.com/google/play/billing

- [ ] **Добавить обработку ошибок**
  - Network errors
  - Firebase timeouts
  - Retry logic

### UI/UX

- [ ] **Добавить загрузочные состояния**
  - Shimmer эффекты
  - Skeleton screens
  - Progress indicators

- [ ] **Реализовать pull-to-refresh**
  - На всех экранах с лентой

- [ ] **Добавить swipe-to-dismiss**
  - Для карточек бомб

- [ ] **Улучшить анимации**
  - Transitions между экранами
  - Анимация таймера
  - Конфетти при попадании в топ

### Тестирование

- [ ] **Написать Unit тесты**
  - Для Repository
  - Для ViewModel
  - Coverage: минимум 60%

- [ ] **Написать UI тесты**
  - Compose UI tests
  - Основные флоу
  - Файл: `app/src/androidTest/`

- [ ] **Протестировать на разных устройствах**
  - Android 7, 9, 11, 13, 14
  - Разные размеры экранов
  - Разные производители

---

## 🟡 ЖЕЛАТЕЛЬНО (Улучшения)

### Функции

- [ ] **Стикеры и фильтры**
  - Библиотека стикеров
  - UI для выбора
  - Наложение на фото

- [ ] **AR-эффекты**
  - ARCore интеграция
  - Маски для лица
  - 3D объекты

- [ ] **Чаты между друзьями**
  - Firebase Realtime Database
  - UI чатов
  - Push на новые сообщения

- [ ] **Групповые челленджи**
  - Создание групп
  - Совместные бомбы
  - Групповой топ

- [ ] **Sharing в соцсети**
  - Instagram Stories API
  - TikTok API
  - Генерация красивых preview

- [ ] **Локализация**
  - Английский (EN)
  - Испанский (ES)
  - Немецкий (DE)

- [ ] **Светлая тема**
  - Material You dynamic colors
  - Переключатель темы в настройках

### Оптимизация

- [ ] **Кэширование**
  - Room database для offline
  - Кэширование изображений
  - Предзагрузка топа

- [ ] **Пагинация**
  - Paging 3 library
  - Для больших списков бомб

- [ ] **Компрессия видео**
  - Улучшить C++ алгоритм
  - H.264/H.265 кодеки
  - Adaptive bitrate

- [ ] **Background загрузка**
  - WorkManager для upload
  - Retry при ошибках
  - Queue система

### Аналитика

- [ ] **Crashlytics**
  - Отслеживание крашей
  - ANR reporting
  - Custom logs

- [ ] **Performance Monitoring**
  - Скорость загрузки
  - Время отклика UI
  - Network latency

- [ ] **Custom Events**
  - Больше событий Analytics
  - Conversion funnels
  - User segments

### Безопасность

- [ ] **Content Moderation**
  - ML Kit для обнаружения неподобающего контента
  - Report система
  - Admin панель для модерации

- [ ] **Rate Limiting**
  - Firestore Rules
  - Cloud Functions throttling
  - Anti-spam меры

- [ ] **Encryption**
  - Encrypted SharedPreferences
  - SSL pinning
  - Sensitive data protection

---

## 🟢 BACKLOG (Будущее)

### v1.1
- [ ] CameraX live camera
- [ ] ML Kit face detection
- [ ] Google Play Billing
- [ ] Crashlytics

### v1.2
- [ ] Стикеры
- [ ] Локализация (EN)
- [ ] Светлая тема
- [ ] Больше достижений

### v2.0
- [ ] Чаты
- [ ] AR-эффекты
- [ ] Групповые челленджи
- [ ] Social sharing API

### v2.x
- [ ] Web версия (PWA)
- [ ] iOS версия
- [ ] Desktop версия (Compose Multiplatform)

---

## 🐛 KNOWN ISSUES

### Критичные
- ❌ Нет: CameraX live preview (только галерея)
- ❌ Нет: ML Kit для лиц (только manual blur)
- ❌ Нет: Google Play Billing (PRO UI есть)

### Некритичные
- ⚠️ Иконки - placeholder PNG (нужны реальные)
- ⚠️ Firebase демо конфиг (нужен настоящий)
- ⚠️ Нет обработки всех edge cases
- ⚠️ Нет offline режима
- ⚠️ Нет retry логики для network

### UX улучшения
- 🔵 Добавить haptic feedback
- 🔵 Улучшить error messages
- 🔵 Добавить onboarding для новых пользователей
- 🔵 Добавить tutorial для камеры
- 🔵 Улучшить empty states

---

## 📝 Как использовать этот список

1. **Перед запуском** - выполните все из секции 🔴 КРИТИЧЕСКИ ВАЖНО
2. **Перед релизом** - выполните из секции 🟠 ВАЖНО
3. **Постепенно** добавляйте из 🟡 ЖЕЛАТЕЛЬНО и 🟢 BACKLOG

### Приоритеты:
- 🔴 Must Have (блокирует запуск)
- 🟠 Should Have (для качественного релиза)
- 🟡 Nice to Have (улучшения)
- 🟢 Future (долгосрочные планы)

---

## 🎯 Прогресс

### v1.0.0 (Текущая)
- [x] Core функционал - 100%
- [x] UI всех экранов - 100%
- [x] Firebase интеграция - 100%
- [x] Документация - 100%
- [ ] CameraX live - 0%
- [ ] ML Kit - 0%
- [ ] Billing - 0%
- [ ] Тесты - 0%

**Общий прогресс v1.0:** ~70% (готов к тестированию)

---

## 💬 Заметки

- Весь TODO отсортирован по приоритетам
- Критичные вещи помечены 🔴
- Время на реализацию v1.0 полностью: ~1 неделя
- v1.1 с основными фичами: ~2 недели
- v2.0 с полным функционалом: ~1-2 месяца

---

_Последнее обновление: 13 февраля 2026_
