package com.example.fitcontrol.feature_members.domain.model

import com.google.gson.annotations.SerializedName

data class DailyReport(
    val date: String,
    @SerializedName("memberships") val memberships: Double,
    @SerializedName("sales") val sales: Double,
    @SerializedName("total") val total: Double
)