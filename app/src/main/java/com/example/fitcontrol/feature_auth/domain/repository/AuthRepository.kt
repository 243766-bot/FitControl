package com.example.fitcontrol.feature_auth.domain.repository

import com.example.fitcontrol.feature_auth.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun register(name: String, email: String, password: String): User?
    fun saveSession(userId: String)
    fun getUserId(): String?
    fun logout()
}