/**
 * Firebase Cloud Functions для БомбаДня
 * 
 * Инструкция по установке:
 * 1. npm install -g firebase-tools
 * 2. firebase login
 * 3. firebase init functions (выберите JavaScript)
 * 4. Скопируйте этот код в functions/index.js
 * 5. cd functions && npm install
 * 6. firebase deploy --only functions
 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();
const messaging = admin.messaging();

/**
 * Запуск ежедневной бомбы
 * 
 * НАСТРОЙКА ВРЕМЕНИ:
 * - '0 19 * * *' = каждый день в 19:00 (Europe/Moscow)
 * - Можно изменить на случайное время через generateRandomTime()
 */
exports.scheduleDailyBomb = functions
  .region('europe-west1')
  .pubsub
  .schedule('0 19 * * *')
  .timeZone('Europe/Moscow')
  .onRun(async (context) => {
    try {
      const now = admin.firestore.Timestamp.now();
      const sessionId = `bomb_${now.toMillis()}`;
      const today = new Date().toISOString().split('T')[0];
      
      // Вычисляем когда закончится голосование (60 сек загрузка + 5 мин голосование)
      const votingEndsAt = new admin.firestore.Timestamp(
        now.seconds + 60 + (5 * 60),
        0
      );
      
      // Создаем новую сессию
      await db.collection('bomb_sessions').doc(sessionId).set({
        id: sessionId,
        date: today,
        launchTime: now,
        captureWindowSeconds: 60,
        votingWindowMinutes: 5,
        votingEndsAt: votingEndsAt,
        totalParticipants: 0,
        status: 'CAPTURING'
      });
      
      console.log(`Created bomb session: ${sessionId}`);
      
      // Отправляем пуш-уведомление всем пользователям
      const message = {
        notification: {
          title: '💣 БОМБА ЗАПУЩЕНА!',
          body: 'У тебя есть 60 секунд! Открывай камеру!'
        },
        data: {
          type: 'bomb_launch',
          sessionId: sessionId,
          click_action: 'OPEN_CAMERA'
        },
        android: {
          priority: 'high',
          notification: {
            channelId: 'bomb_launch',
            priority: 'high',
            sound: 'default',
            vibrate_timings_millis: [0, 500, 200, 500]
          }
        },
        topic: 'all_users'
      };
      
      await messaging.send(message);
      console.log('Sent notification to all users');
      
      // Через 60 секунд меняем статус на VOTING
      setTimeout(async () => {
        await db.collection('bomb_sessions').doc(sessionId).update({
          status: 'VOTING'
        });
        console.log(`Session ${sessionId} now in VOTING phase`);
      }, 60000);
      
      return { success: true, sessionId };
      
    } catch (error) {
      console.error('Error in scheduleDailyBomb:', error);
      return { success: false, error: error.message };
    }
  });

/**
 * Завершение голосования и подсчет результатов
 * Запускается каждые 5 минут для проверки
 */
exports.endVotingSession = functions
  .region('europe-west1')
  .pubsub
  .schedule('every 5 minutes')
  .onRun(async (context) => {
    try {
      const now = admin.firestore.Timestamp.now();
      
      // Находим сессии где голосование должно закончиться
      const sessionsSnapshot = await db.collection('bomb_sessions')
        .where('status', '==', 'VOTING')
        .where('votingEndsAt', '<=', now)
        .get();
      
      if (sessionsSnapshot.empty) {
        console.log('No sessions to end');
        return { processed: 0 };
      }
      
      const batch = db.batch();
      let processedCount = 0;
      
      for (const sessionDoc of sessionsSnapshot.docs) {
        const sessionId = sessionDoc.id;
        
        // Обновляем статус сессии
        batch.update(sessionDoc.ref, { status: 'COMPLETED' });
        
        // Получаем топ-10 бомб этой сессии
        const bombsSnapshot = await db.collection('bombs')
          .where('bombSessionId', '==', sessionId)
          .orderBy('totalVotes', 'desc')
          .limit(10)
          .get();
        
        // Отправляем уведомления участникам о результатах
        const notifications = [];
        
        bombsSnapshot.docs.forEach((bombDoc, index) => {
          const bomb = bombDoc.data();
          const position = index + 1;
          
          let title, body;
          if (position === 1) {
            title = '🏆 ТЫ ПЕРВЫЙ!';
            body = 'Поздравляем! Ты король дня!';
          } else if (position <= 3) {
            title = `🥇 ТЫ НА ${position} МЕСТЕ!`;
            body = `Отличная бомба! Ты в топ-3!`;
          } else {
            title = `🔥 ТЫ В ТОП-10!`;
            body = `Твоя позиция: #${position}`;
          }
          
          // TODO: Отправлять конкретному пользователю через его FCM token
          // Сейчас отправляем через топик (для демо)
          notifications.push({
            notification: { title, body },
            data: {
              type: 'voting_end',
              position: position.toString(),
              sessionId: sessionId,
              bombId: bombDoc.id
            },
            topic: `user_${bomb.deviceId}` // Пользователь должен подписаться на свой топик
          });
        });
        
        // Отправляем уведомления
        for (const notification of notifications) {
          try {
            await messaging.send(notification);
          } catch (error) {
            console.error('Error sending notification:', error);
          }
        }
        
        processedCount++;
      }
      
      await batch.commit();
      console.log(`Ended ${processedCount} voting sessions`);
      
      return { processed: processedCount };
      
    } catch (error) {
      console.error('Error in endVotingSession:', error);
      return { success: false, error: error.message };
    }
  });

/**
 * Начисление монет за попадание в топ
 * Триггерится при обновлении сессии на COMPLETED
 */
exports.awardTopBombers = functions
  .region('europe-west1')
  .firestore
  .document('bomb_sessions/{sessionId}')
  .onUpdate(async (change, context) => {
    const newData = change.after.data();
    const oldData = change.before.data();
    
    // Проверяем что статус изменился на COMPLETED
    if (oldData.status !== 'COMPLETED' && newData.status === 'COMPLETED') {
      const sessionId = context.params.sessionId;
      
      try {
        // Получаем топ-10
        const bombsSnapshot = await db.collection('bombs')
          .where('bombSessionId', '==', sessionId)
          .orderBy('totalVotes', 'desc')
          .limit(10)
          .get();
        
        const batch = db.batch();
        
        bombsSnapshot.docs.forEach((bombDoc, index) => {
          const bomb = bombDoc.data();
          const userId = bomb.deviceId;
          const position = index + 1;
          
          // Начисляем монеты в зависимости от позиции
          let coinsReward = 0;
          if (position === 1) coinsReward = 100;
          else if (position === 2) coinsReward = 75;
          else if (position === 3) coinsReward = 50;
          else coinsReward = 25;
          
          // Обновляем профиль пользователя
          const userRef = db.collection('users').doc(userId);
          batch.update(userRef, {
            coins: admin.firestore.FieldValue.increment(coinsReward),
            topTenCount: admin.firestore.FieldValue.increment(1)
          });
          
          // Проверяем достижение "Король дня"
          if (position === 1) {
            batch.update(userRef, {
              achievements: admin.firestore.FieldValue.arrayUnion('top_1')
            });
          }
        });
        
        await batch.commit();
        console.log(`Awarded coins to top ${bombsSnapshot.size} bombers`);
        
      } catch (error) {
        console.error('Error awarding top bombers:', error);
      }
    }
    
    return null;
  });

/**
 * Очистка старых сессий (удаление сессий старше 7 дней)
 * Запускается каждую ночь в 3:00
 */
exports.cleanupOldSessions = functions
  .region('europe-west1')
  .pubsub
  .schedule('0 3 * * *')
  .timeZone('Europe/Moscow')
  .onRun(async (context) => {
    try {
      const sevenDaysAgo = new Date();
      sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);
      const cutoffDate = sevenDaysAgo.toISOString().split('T')[0];
      
      const oldSessionsSnapshot = await db.collection('bomb_sessions')
        .where('date', '<', cutoffDate)
        .get();
      
      if (oldSessionsSnapshot.empty) {
        console.log('No old sessions to clean');
        return { deleted: 0 };
      }
      
      const batch = db.batch();
      oldSessionsSnapshot.docs.forEach(doc => {
        batch.delete(doc.ref);
      });
      
      await batch.commit();
      console.log(`Deleted ${oldSessionsSnapshot.size} old sessions`);
      
      return { deleted: oldSessionsSnapshot.size };
      
    } catch (error) {
      console.error('Error in cleanupOldSessions:', error);
      return { success: false, error: error.message };
    }
  });

/**
 * ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ
 */

// Генерация случайного времени в диапазоне
function generateRandomTime(startHour = 18, endHour = 21) {
  const hour = Math.floor(Math.random() * (endHour - startHour + 1)) + startHour;
  const minute = Math.floor(Math.random() * 60);
  
  return {
    hour,
    minute,
    cron: `${minute} ${hour} * * *`
  };
}

// Модерация контента (базовая проверка)
// TODO: Интегрировать ML Kit для обнаружения неподобающего контента
async function moderateContent(imageUrl) {
  // Placeholder для будущей реализации
  return { approved: true, reason: null };
}

/**
 * DEPLOYMENT:
 * 
 * 1. Установка Firebase CLI:
 *    npm install -g firebase-tools
 * 
 * 2. Логин в Firebase:
 *    firebase login
 * 
 * 3. Инициализация проекта:
 *    firebase init functions
 * 
 * 4. Установка зависимостей:
 *    cd functions
 *    npm install
 * 
 * 5. Деплой функций:
 *    firebase deploy --only functions
 * 
 * 6. Деплой конкретной функции:
 *    firebase deploy --only functions:scheduleDailyBomb
 * 
 * 7. Просмотр логов:
 *    firebase functions:log
 * 
 * 8. Тестирование локально:
 *    firebase emulators:start
 */
