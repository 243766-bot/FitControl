package com.example.fitcontrol.feature_members.data.repository

import com.example.fitcontrol.core.data.local.dao.MemberDao
import com.example.fitcontrol.core.data.local.dao.ProductDao
import com.example.fitcontrol.core.data.local.entities.MemberEntity
import com.example.fitcontrol.core.data.local.entities.ProductEntity
import com.example.fitcontrol.feature_auth.domain.repository.AuthRepository
import com.example.fitcontrol.feature_members.data.remote.MemberApi
import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.model.MemberStatus
import com.example.fitcontrol.feature_members.domain.model.Product
import com.example.fitcontrol.feature_members.domain.model.InventoryMovement
import com.example.fitcontrol.feature_members.domain.model.DailyReport
import com.example.fitcontrol.feature_members.domain.model.Membership
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val api: MemberApi,
    private val memberDao: MemberDao,
    private val productDao: ProductDao,
    private val authRepository: AuthRepository
) : MemberRepository {

    // --- LÓGICA DE SOCIOS (Offline-First) ---
    override fun getMembers(): Flow<List<Member>> = flow {
        val userId = authRepository.getUserId() ?: ""

        // 1. Intentar emitir lo que hay en Room primero
        try {
            // Obtenemos la lista de la base de datos local
            val localEntities: List<MemberEntity> = memberDao.getMembers(userId).first()
            // Convertimos la lista de entidades a lista de modelos de negocio
            val domainMembers = localEntities.map { entity -> entity.toDomain() }
            emit(domainMembers)
        } catch (e: Exception) {
            emit(emptyList<Member>())
        }

        // 2. Sincronizar con la nube (AWS)
        try {
            val remoteMembers = api.getMembers(userId)
            // Guardar en Room para que esté disponible offline
            val entitiesToInsert = remoteMembers.map { member -> member.toEntity() }
            memberDao.insertMembers(entitiesToInsert)
            // Emitir los datos frescos
            emit(remoteMembers)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- LÓGICA DE PRODUCTOS (Offline-First) ---
    override suspend fun getProducts(): List<Product> {
        val userId = authRepository.getUserId() ?: ""
        return try {
            val remoteProducts = api.getProducts(userId)
            productDao.insertProducts(remoteProducts.map { it.toEntity() })
            remoteProducts
        } catch (e: Exception) {
            productDao.getProducts(userId).map { it.toDomain() }
        }
    }

    override suspend fun saveProduct(product: Product) {
        val userId = authRepository.getUserId() ?: ""
        try {
            api.saveProduct(product.copy(user_id = userId))
            productDao.insertProducts(listOf(product.copy(user_id = userId).toEntity()))
        } catch (e: Exception) { e.printStackTrace() }
    }

    override suspend fun insertMember(member: Member) {
        val userId = authRepository.getUserId() ?: ""
        try {
            api.saveMember(member.copy(user_id = userId))
            memberDao.insertMembers(listOf(member.copy(user_id = userId).toEntity()))
        } catch (e: Exception) { e.printStackTrace() }
    }

    // --- MAPPERS (Conversores) ---
    private fun ProductEntity.toDomain() = Product(
        id = id, user_id = user_id, name = name, category = category,
        buy_price = buy_price, sell_price = sell_price, stock = stock, min_stock = min_stock
    )

    private fun Product.toEntity() = ProductEntity(
        id = id ?: 0, user_id = user_id ?: "", name = name, category = category,
        buy_price = buy_price, sell_price = sell_price, stock = stock, min_stock = min_stock
    )

    private fun MemberEntity.toDomain() = Member(
        id = id, user_id = user_id, name = name, phone = phone, email = email,
        birthDate = birthDate, photoUrl = photoUrl, status = MemberStatus.valueOf(status)
    )

    private fun Member.toEntity() = MemberEntity(
        id = id, user_id = user_id ?: "", name = name, phone = phone, email = email,
        birthDate = birthDate, photoUrl = photoUrl, status = status.name
    )

    // --- RESTO DE FUNCIONES ---
    override suspend fun getMemberById(id: String): Member? = try { api.getMemberById(id) } catch (e: Exception) { null }
    override suspend fun deleteMember(member: Member) { try { api.deleteMember(member.id) } catch (e: Exception) { } }
    override suspend fun updateMember(member: Member) { try { api.updateMember(member.id, member) } catch (e: Exception) { } }
    override suspend fun insertMembership(membership: Membership) { try { api.saveMembership(membership) } catch (e: Exception) { } }
    override suspend fun getMembershipsByMemberId(id: String): List<Membership> = try { api.getMembershipsByMemberId(id) } catch (e: Exception) { emptyList() }
    override suspend fun registerMovement(movement: InventoryMovement) { try { api.registerMovement(movement) } catch (e: Exception) { } }
    override suspend fun getDailyReport(): DailyReport {
        val userId = authRepository.getUserId() ?: ""
        return try { api.getDailyReport(userId) } catch (e: Exception) { DailyReport("", 0.0, 0.0, 0.0) }
    }
}