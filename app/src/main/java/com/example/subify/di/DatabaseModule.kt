package com.example.subify.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.subify.data.local.SubifyDatabase
import com.example.subify.data.local.SubscriptionDao
import com.example.subify.data.model.BillingCycle
import com.example.subify.data.model.Subscription
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        subscriptionDaoProvider: Provider<SubscriptionDao>
    ): SubifyDatabase {
        return Room.databaseBuilder(
            context,
            SubifyDatabase::class.java,
            "subify_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = subscriptionDaoProvider.get()
                    val now = System.currentTimeMillis()
                    val mockSubscriptions = listOf(
                        Subscription(name = "Steam Game Pass", cost = 209.0, currency = "₺", billingCycle = BillingCycle.MONTHLY, nextPaymentDate = now + 3 * 24 * 60 * 60 * 1000L, category = "Gaming", colorHex = "#FF1A75"),
                        Subscription(name = "Netflix Premium", cost = 15.99, currency = "$", billingCycle = BillingCycle.MONTHLY, nextPaymentDate = now + 5 * 24 * 60 * 60 * 1000L, category = "Entertainment", colorHex = "#E50914"),
                        Subscription(name = "Spotify Premium", cost = 59.99, currency = "₺", billingCycle = BillingCycle.MONTHLY, nextPaymentDate = now + 1 * 24 * 60 * 60 * 1000L, category = "Entertainment", colorHex = "#1DB954"),
                        Subscription(name = "Google One 2TB", cost = 289.99, currency = "₺", billingCycle = BillingCycle.YEARLY, nextPaymentDate = now + 15 * 24 * 60 * 60 * 1000L, category = "Finance", colorHex = "#4285F4"),
                        Subscription(name = "Papara Virtual Card", cost = 120.0, currency = "₺", billingCycle = BillingCycle.WEEKLY, nextPaymentDate = now + 2 * 24 * 60 * 60 * 1000L, category = "Finance", colorHex = "#FF007F")
                    )
                    for (subscription in mockSubscriptions) {
                        dao.insertSubscription(subscription)
                    }
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideSubscriptionDao(database: SubifyDatabase): SubscriptionDao {
        return database.subscriptionDao()
    }
}
