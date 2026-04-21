package com.example.fitcontrol.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitcontrol.core.data.local.dao.MemberDao
import com.example.fitcontrol.core.data.local.dao.ProductDao
import com.example.fitcontrol.core.data.local.entities.MemberEntity
// Corregimos el import quitando el "import" pegado a la carpeta

import com.example.fitcontrol.core.data.local.entities.ProductEntity


@Database(
    entities = [
        MemberEntity::class,
        ProductEntity::class // <--- AGREGADO: Para cumplir con el MVP 02 (Inventario)
    ],
    version = 1,
    exportSchema = false
)
abstract class FitControlDatabase : RoomDatabase() {
    abstract val memberDao: MemberDao
    abstract val productDao: ProductDao
}