package com.example.fitcontrol.feature_auth.presentation.register

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)