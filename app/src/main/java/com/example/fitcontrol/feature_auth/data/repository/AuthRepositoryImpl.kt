package com.example.fitcontrol.feature_auth.data.repository

import android.content.Context
import com.example.fitcontrol.feature_auth.data.remote.AuthApi
import com.example.fitcontrol.feature_auth.domain.model.LoginRequest
import com.example.fitcontrol.feature_auth.domain.model.User
import com.example.fitcontrol.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val prefs = context.getSharedPreferences("fitcontrol_prefs", Context.MODE_PRIVATE)

    override suspend fun login(email: String, password: String): User? {
        return try {
            val user = api.login(LoginRequest(email, password))
            user.id?.let { saveSession(it) }
            user
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun register(name: String, email: String, password: String): User? {
        return try {
            val user = api.register(User(name = name, email = email, password = password))
            user.id?.let { saveSession(it) }
            user
        } catch (e: Exception) {
            null
        }
    }

    override fun saveSession(userId: String) {
        prefs.edit().putString("user_id", userId).apply()
    }

    override fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    override fun logout() {
        prefs.edit().remove("user_id").apply()
    }
}