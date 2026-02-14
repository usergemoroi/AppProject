package com.bombaday.app.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bombaday.app.model.Bomb
import com.bombaday.app.model.BombSession
import com.bombaday.app.model.SessionStatus
import com.bombaday.app.model.UserProfile
import com.bombaday.app.repository.BombRepository
import com.bombaday.app.repository.UserRepository
import com.bombaday.app.util.Constants
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val bombRepository = BombRepository()
    private val userRepository = UserRepository()
    
    private val _currentSession = MutableStateFlow<BombSession?>(null)
    val currentSession: StateFlow<BombSession?> = _currentSession.asStateFlow()
    
    private val _topBombs = MutableStateFlow<List<Bomb>>(emptyList())
    val topBombs: StateFlow<List<Bomb>> = _topBombs.asStateFlow()
    
    private val _allBombs = MutableStateFlow<List<Bomb>>(emptyList())
    val allBombs: StateFlow<List<Bomb>> = _allBombs.asStateFlow()
    
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()
    
    private val _userBomb = MutableStateFlow<Bomb?>(null)
    val userBomb: StateFlow<Bomb?> = _userBomb.asStateFlow()
    
    private val _timeRemaining = MutableStateFlow(0L)
    val timeRemaining: StateFlow<Long> = _timeRemaining.asStateFlow()
    
    private val _votingTimeRemaining = MutableStateFlow(0L)
    val votingTimeRemaining: StateFlow<Long> = _votingTimeRemaining.asStateFlow()
    
    private val _uploadProgress = MutableStateFlow(false)
    val uploadProgress: StateFlow<Boolean> = _uploadProgress.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _selectedEmojis = MutableStateFlow<Map<String, String>>(emptyMap())
    val selectedEmojis: StateFlow<Map<String, String>> = _selectedEmojis.asStateFlow()
    
    private var deviceId: String = ""
    
    init {
        deviceId = getOrCreateDeviceId()
        loadUserProfile()
        loadCurrentSession()
        startTimerUpdates()
    }
    
    private fun getOrCreateDeviceId(): String {
        val prefs = getApplication<Application>().getSharedPreferences("bombaday", Context.MODE_PRIVATE)
        var id = prefs.getString(Constants.PREF_DEVICE_ID, null)
        
        if (id == null) {
            id = UUID.randomUUID().toString()
            prefs.edit().putString(Constants.PREF_DEVICE_ID, id).apply()
        }
        
        return id
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            val profile = userRepository.getUserProfile(deviceId) 
                ?: userRepository.createOrUpdateUser(deviceId, deviceId)
            _userProfile.value = profile
        }
    }
    
    fun loadCurrentSession() {
        viewModelScope.launch {
            val session = bombRepository.getCurrentSession()
            _currentSession.value = session

            if (session != null) {
                loadTopBombs(session.id)
                loadAllBombs(session.id)
                loadUserBomb(session.id)
            }
        }
    }
    
    private fun loadTopBombs(sessionId: String) {
        viewModelScope.launch {
            val bombs = bombRepository.getTopBombs(sessionId)
            _topBombs.value = bombs
        }
    }
    
    private fun loadAllBombs(sessionId: String) {
        viewModelScope.launch {
            val bombs = bombRepository.getTodaysBombs(sessionId)
            _allBombs.value = bombs
            loadUserVotes(sessionId)
        }
    }

    private fun loadUserBomb(sessionId: String) {
        viewModelScope.launch {
            val bomb = bombRepository.getUserBombForSession(deviceId, sessionId)
            _userBomb.value = bomb
        }
    }

    private fun loadUserVotes(sessionId: String) {
        viewModelScope.launch {
            val votes = mutableMapOf<String, String>()
            _allBombs.value.forEach { bomb ->
                Constants.AVAILABLE_EMOJIS.forEach { emoji ->
                    if (bombRepository.hasVotedForBomb(bomb.id, deviceId)) {
                        votes[bomb.id] = emoji
                    }
                }
            }
            _selectedEmojis.value = votes
        }
    }
    
    fun uploadBomb(mediaUri: Uri, isVideo: Boolean, isBlurred: Boolean) {
        val session = _currentSession.value ?: return
        
        if (session.status != SessionStatus.CAPTURING) {
            _errorMessage.value = "Время для загрузки бомбы истекло"
            return
        }
        
        viewModelScope.launch {
            _uploadProgress.value = true
            
            val result = bombRepository.uploadBomb(
                mediaUri = mediaUri,
                isVideo = isVideo,
                deviceId = deviceId,
                isBlurred = isBlurred,
                sessionId = session.id
            )
            
            _uploadProgress.value = false
            
            if (result.isSuccess) {
                // Update streak
                val newStreak = userRepository.updateStreak(deviceId)
                val profile = _userProfile.value
                if (profile != null) {
                    _userProfile.value = profile.copy(streak = newStreak)
                    userRepository.checkAndUnlockAchievements(deviceId, profile)
                }
                
                loadUserBomb(session.id)
                loadAllBombs(session.id)
            } else {
                _errorMessage.value = "Ошибка загрузки: ${result.exceptionOrNull()?.message}"
            }
        }
    }
    
    fun voteBomb(bombId: String, emoji: String) {
        viewModelScope.launch {
            val result = bombRepository.voteBomb(bombId, deviceId, emoji)
            
            if (result.isSuccess) {
                _selectedEmojis.value = _selectedEmojis.value + (bombId to emoji)
                
                val session = _currentSession.value
                if (session != null) {
                    loadTopBombs(session.id)
                    loadAllBombs(session.id)
                }
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    private fun startTimerUpdates() {
        viewModelScope.launch {
            while (true) {
                val session = _currentSession.value
                if (session != null) {
                    when (session.status) {
                        SessionStatus.CAPTURING -> {
                            val launchTime = session.launchTime.toDate().time
                            val endTime = launchTime + (Constants.CAPTURE_WINDOW_SECONDS * 1000)
                            val remaining = (endTime - System.currentTimeMillis()) / 1000
                            _timeRemaining.value = maxOf(0, remaining)
                            
                            if (remaining <= 0) {
                                loadCurrentSession()
                            }
                        }
                        SessionStatus.VOTING -> {
                            val votingEnd = session.votingEndsAt.toDate().time
                            val remaining = (votingEnd - System.currentTimeMillis()) / 1000
                            _votingTimeRemaining.value = maxOf(0, remaining)
                            
                            if (remaining <= 0) {
                                loadCurrentSession()
                            }
                        }
                        else -> {
                            _timeRemaining.value = 0
                            _votingTimeRemaining.value = 0
                        }
                    }
                }
                
                delay(1000)
            }
        }
    }
    
    fun getUserDeviceId(): String = deviceId
    
    fun refreshData() {
        loadCurrentSession()
    }
}
