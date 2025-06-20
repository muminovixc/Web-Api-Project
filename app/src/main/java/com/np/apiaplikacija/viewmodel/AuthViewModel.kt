package com.np.apiaplikacija.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.np.apiaplikacija.data.db.AppDatabase
import com.np.apiaplikacija.data.db.UserEntity
import com.np.apiaplikacija.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repo = AuthRepository(db.userDao())
    private val preferences = application.getSharedPreferences("user_prefs", Application.MODE_PRIVATE)

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _registerSuccess = MutableStateFlow<Boolean?>(null)
    val registerSuccess: StateFlow<Boolean?> = _registerSuccess

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repo.login(email, password)
            if (user != null) {
                // ✅ Spasi user_id u SharedPreferences
                preferences.edit().putInt("user_id", user.id).apply()
                _loginSuccess.value = true
            } else {
                _loginSuccess.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val success = repo.register(UserEntity(name = name, email = email, password = password))
            _registerSuccess.value = success
        }
    }
}
