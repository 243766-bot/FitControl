package com.example.fitcontrol.feature_auth.data.remote

import com.example.fitcontrol.feature_auth.domain.model.User
import com.example.fitcontrol.feature_auth.domain.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): User

    @POST("auth/register")
    suspend fun register(@Body user: User): User
}