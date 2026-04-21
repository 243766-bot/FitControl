package com.example.fitcontrol.feature_members.domain.repository

import com.example.fitcontrol.feature_members.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getMembers(): Flow<List<Member>>
    suspend fun getMemberById(id: String): Member?
    suspend fun insertMember(member: Member)
    suspend fun deleteMember(member: Member)
    suspend fun updateMember(member: Member)
    
    suspend fun insertMembership(membership: Membership)
    suspend fun getMembershipsByMemberId(id: String): List<Membership>

    suspend fun getProducts(): List<Product>
    suspend fun saveProduct(product: Product)
    suspend fun registerMovement(movement: InventoryMovement)

    suspend fun getDailyReport(): DailyReport
}