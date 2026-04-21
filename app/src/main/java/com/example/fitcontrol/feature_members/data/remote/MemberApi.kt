package com.example.fitcontrol.feature_members.data.remote

import com.example.fitcontrol.feature_members.domain.model.*
import retrofit2.http.*

interface MemberApi {
    @GET("members/{userId}")
    suspend fun getMembers(@Path("userId") userId: String): List<Member>

    @GET("members/detail/{id}")
    suspend fun getMemberById(@Path("id") id: String): Member

    @POST("members")
    suspend fun saveMember(@Body member: Member): Member

    @PUT("members/{id}")
    suspend fun updateMember(@Path("id") id: String, @Body member: Member)

    @DELETE("members/{id}")
    suspend fun deleteMember(@Path("id") id: String)

    @POST("memberships")
    suspend fun saveMembership(@Body membership: Membership)

    @GET("memberships/member/{id}")
    suspend fun getMembershipsByMemberId(@Path("id") id: String): List<Membership>

    @GET("products/{userId}")
    suspend fun getProducts(@Path("userId") userId: String): List<Product>

    @POST("products")
    suspend fun saveProduct(@Body product: Product)

    @POST("inventory/move")
    suspend fun registerMovement(@Body movement: InventoryMovement)

    @GET("reports/daily/{userId}")
    suspend fun getDailyReport(@Path("userId") userId: String): DailyReport

    companion object {

        const val BASE_URL = "http://18.205.240.72:3000/"
    }
}