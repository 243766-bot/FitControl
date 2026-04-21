package com.example.fitcontrol.core.di

import android.content.Context
import androidx.room.Room
import com.example.fitcontrol.core.data.local.FitControlDatabase
import com.example.fitcontrol.core.data.local.dao.MemberDao
import com.example.fitcontrol.core.data.local.dao.ProductDao
import com.example.fitcontrol.feature_auth.data.remote.AuthApi
import com.example.fitcontrol.feature_auth.data.repository.AuthRepositoryImpl
import com.example.fitcontrol.feature_auth.domain.repository.AuthRepository
import com.example.fitcontrol.feature_members.data.remote.MemberApi
import com.example.fitcontrol.feature_members.data.repository.MemberRepositoryImpl
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MemberApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApi(retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FitControlDatabase {
        return Room.databaseBuilder(
            context,
            FitControlDatabase::class.java,
            "fitcontrol_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMemberDao(db: FitControlDatabase): MemberDao {
        return db.memberDao
    }

    @Provides
    @Singleton
    fun provideProductDao(db: FitControlDatabase): ProductDao {
        return db.productDao
    }

} // <--- Aquí faltaba cerrar esta llave

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(
        memberRepositoryImpl: MemberRepositoryImpl
    ): MemberRepository
}