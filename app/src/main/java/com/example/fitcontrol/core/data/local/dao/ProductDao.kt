package com.example.fitcontrol.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitcontrol.core.data.local.entities.ProductEntity // Import correcto

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE user_id = :userId")
    suspend fun getProducts(userId: String): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)
}