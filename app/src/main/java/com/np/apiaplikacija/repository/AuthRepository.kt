package com.np.apiaplikacija.repository

import com.np.apiaplikacija.data.db.UserDao
import com.np.apiaplikacija.data.db.UserEntity

class AuthRepository(private val userDao: UserDao) {
    suspend fun register(user: UserEntity): Boolean {
        val existing = userDao.getUserByEmail(user.email)
        return if (existing == null) {
            userDao.insert(user)
            true
        } else false
    }

    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }
}
