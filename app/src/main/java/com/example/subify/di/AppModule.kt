package com.example.subify.di

import com.example.subify.data.local.SubscriptionDao
import com.example.subify.data.repository.SubscriptionRepository
import com.example.subify.data.repository.SubscriptionRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideSubscriptionRepository(
        subscriptionDao: SubscriptionDao,
        firestore: FirebaseFirestore
    ): SubscriptionRepository {
        return SubscriptionRepositoryImpl(subscriptionDao, firestore)
    }
}
