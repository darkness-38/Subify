package com.example.subify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.subify.data.model.Subscription

@Database(entities = [Subscription::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SubifyDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
}
