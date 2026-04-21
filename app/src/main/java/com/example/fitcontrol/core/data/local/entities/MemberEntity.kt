package com.example.fitcontrol.core.data.local.entities

import androidx.room.Entity



import androidx.room.PrimaryKey




@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val user_id: String,
    val name: String,
    val phone: String,
    val email: String,
    val birthDate: String,
    val photoUrl: String?,
    val status: String
)