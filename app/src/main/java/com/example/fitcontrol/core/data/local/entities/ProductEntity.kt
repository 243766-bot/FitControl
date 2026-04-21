package com.example.fitcontrol.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val user_id: String,
    val name: String,
    val category: String,
    val buy_price: Double,
    val sell_price: Double,
    val stock: Int,
    val min_stock: Int
)