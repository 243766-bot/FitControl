package com.example.fitcontrol.feature_members.presentation.add_member

data class AddMemberState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val capturedImageUri: String? = null
)