package com.example.fitcontrol.feature_members.domain.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Member(
    // Generamos el ID único para MySQL
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("user_id")
    val user_id: String? = null,

    val name: String,
    val phone: String,
    val email: String,

    @SerializedName("birthDate")
    val birthDate: String = "2000-01-01",

    @SerializedName("photoUrl")
    val photoUrl: String? = null,

    @SerializedName("status")
    val status: MemberStatus = MemberStatus.ACTIVE,

    // --- NUEVOS CAMPOS PARA LA LÓGICA DE VENCIMIENTO ---
    @SerializedName("expiry_date")
    val expiryDate: String? = null,

    @SerializedName("days_left")
    val daysLeft: Int? = null
)

enum class MemberStatus {
    @SerializedName("ACTIVE") ACTIVE,
    @SerializedName("INACTIVE") INACTIVE,
    @SerializedName("EXPIRED") EXPIRED
}