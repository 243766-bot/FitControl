package com.example.fitcontrol.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object AddMember : Screen

    @Serializable
    data class Payment(val memberId: String, val memberName: String) : Screen
}