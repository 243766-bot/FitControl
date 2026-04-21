package com.example.fitcontrol.feature_members.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Membership(
    val id: Int? = null,
    val member_id: String,
    val plan_name: String,
    val amount: Double,
    val payment_date: String? = null,
    val expiry_date: String
)