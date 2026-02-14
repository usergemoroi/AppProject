package com.bombaday.app.repository

import android.net.Uri
import com.bombaday.app.model.Bomb
import com.bombaday.app.model.BombSession
import com.bombaday.app.model.SessionStatus
import com.bombaday.app.model.Vote
import com.bombaday.app.util.Constants
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BombRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    
    private val bombsCollection = firestore.collection(Constants.COLLECTION_BOMBS)
    private val sessionsCollection = firestore.collection(Constants.COLLECTION_SESSIONS)
    private val votesCollection = firestore.collection(Constants.COLLECTION_VOTES)
    
    suspend fun uploadBomb(
        mediaUri: Uri,
        isVideo: Boolean,
        deviceId: String,
        isBlurred: Boolean,
        sessionId: String
    ): Result<String> {
        return try {
            val bombId = UUID.randomUUID().toString()
            val storageRef = storage.reference
                .child(Constants.STORAGE_BOMBS)
                .child(sessionId)
                .child("$bombId.${if (isVideo) "mp4" else "jpg"}")
            
            storageRef.putFile(mediaUri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            
            val bomb = Bomb(
                id = bombId,
                userId = deviceId,
                mediaUrl = downloadUrl.toString(),
                mediaType = if (isVideo) com.bombaday.app.model.MediaType.VIDEO 
                           else com.bombaday.app.model.MediaType.IMAGE,
                timestamp = Timestamp.now(),
                isBlurred = isBlurred,
                deviceId = deviceId,
                bombSessionId = sessionId,
                reactions = mapOf(
                    Constants.EMOJI_FIRE to 0,
                    Constants.EMOJI_SKULL to 0,
                    Constants.EMOJI_CLOWN to 0
                )
            )
            
            bombsCollection.document(bombId).set(bomb).await()
            Result.success(bombId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentSession(): BombSession? {
        return try {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val snapshot = sessionsCollection
                .whereEqualTo("date", today)
                .limit(1)
                .get()
                .await()
            
            if (!snapshot.isEmpty) {
                snapshot.documents.first().toObject(BombSession::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getTodaysBombs(sessionId: String): List<Bomb> {
        return try {
            val snapshot = bombsCollection
                .whereEqualTo("bombSessionId", sessionId)
                .orderBy("totalVotes", Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { it.toObject(Bomb::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getTopBombs(sessionId: String, limit: Int = Constants.TOP_BOMBS_LIMIT): List<Bomb> {
        return try {
            val snapshot = bombsCollection
                .whereEqualTo("bombSessionId", sessionId)
                .orderBy("totalVotes", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { it.toObject(Bomb::class.java) }
                .mapIndexed { index, bomb -> bomb.copy(position = index + 1) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun voteBomb(bombId: String, userId: String, emoji: String): Result<Unit> {
        return try {
            val voteId = "${userId}_${bombId}"
            
            // Check if already voted
            val existingVote = votesCollection.document(voteId).get().await()
            if (existingVote.exists()) {
                return Result.failure(Exception("Already voted"))
            }
            
            // Create vote
            val vote = Vote(
                bombId = bombId,
                userId = userId,
                emoji = emoji,
                timestamp = Timestamp.now()
            )
            
            votesCollection.document(voteId).set(vote).await()
            
            // Update bomb reactions
            firestore.runTransaction { transaction ->
                val bombRef = bombsCollection.document(bombId)
                val bomb = transaction.get(bombRef).toObject(Bomb::class.java)
                
                if (bomb != null) {
                    val newReactions = bomb.reactions.toMutableMap()
                    newReactions[emoji] = (newReactions[emoji] ?: 0) + 1
                    
                    val newTotalVotes = newReactions.values.sum()
                    
                    transaction.update(bombRef, mapOf(
                        "reactions" to newReactions,
                        "totalVotes" to newTotalVotes
                    ))
                }
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun hasVotedForBomb(bombId: String, userId: String): Boolean {
        return try {
            val voteId = "${userId}_${bombId}"
            votesCollection.document(voteId).get().await().exists()
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun getUserBombForSession(userId: String, sessionId: String): Bomb? {
        return try {
            val snapshot = bombsCollection
                .whereEqualTo("deviceId", userId)
                .whereEqualTo("bombSessionId", sessionId)
                .limit(1)
                .get()
                .await()
            
            if (!snapshot.isEmpty) {
                snapshot.documents.first().toObject(Bomb::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    fun observeTopBombs(sessionId: String): Flow<List<Bomb>> = flow {
        bombsCollection
            .whereEqualTo("bombSessionId", sessionId)
            .orderBy("totalVotes", Query.Direction.DESCENDING)
            .limit(Constants.TOP_BOMBS_LIMIT.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val bombs = snapshot.documents.mapNotNull { it.toObject(Bomb::class.java) }
                    .mapIndexed { index, bomb -> bomb.copy(position = index + 1) }
                trySend(bombs)
            }
    }
}
