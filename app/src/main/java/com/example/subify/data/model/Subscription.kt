package com.example.subify.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class BillingCycle {
    WEEKLY, MONTHLY, YEARLY
}

@Serializable
@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val cost: Double,
    val currency: String,
    val billingCycle: BillingCycle,
    val nextPaymentDate: Long, // Epoch timestamp in milliseconds
    val category: String,
    val colorHex: String
)
