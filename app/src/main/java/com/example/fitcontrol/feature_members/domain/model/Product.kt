package com.example.fitcontrol.feature_members.domain.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int? = null,

    @SerializedName("user_id")
    val user_id: String? = null,

    val name: String,
    val category: String,

    @SerializedName("buy_price")
    val buy_price: Double,

    @SerializedName("sell_price")
    val sell_price: Double,

    val stock: Int,

    @SerializedName("min_stock")
    val min_stock: Int
)

data class InventoryMovement(
    @SerializedName("product_id")
    val product_id: Int,

    val type: String,
    val quantity: Int,
    val reason: String
)