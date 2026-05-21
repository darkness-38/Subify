package com.example.subify.data.repository

import com.example.subify.data.local.SubscriptionDao
import com.example.subify.data.model.Subscription
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface SubscriptionRepository {
    fun getAllSubscriptions(): Flow<List<Subscription>>
    fun getSubscriptionById(id: Int): Flow<Subscription?>
    suspend fun insertSubscription(subscription: Subscription)
    suspend fun deleteSubscription(subscription: Subscription)
    fun syncWithFirebase(onComplete: (Boolean) -> Unit)
}

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val subscriptionDao: SubscriptionDao,
    private val firestore: FirebaseFirestore
) : SubscriptionRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getAllSubscriptions(): Flow<List<Subscription>> =
        subscriptionDao.getAllSubscriptions()

    override fun getSubscriptionById(id: Int): Flow<Subscription?> =
        subscriptionDao.getSubscriptionById(id)

    override suspend fun insertSubscription(subscription: Subscription) {
        val insertedId = subscriptionDao.insertSubscription(subscription)
        val finalSubscription = if (subscription.id == 0) {
            subscription.copy(id = insertedId.toInt())
        } else {
            subscription
        }
        
        // Sync to Firebase Firestore asynchronously
        scope.launch {
            try {
                firestore.collection("subscriptions")
                    .document(finalSubscription.id.toString())
                    .set(finalSubscription)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteSubscription(subscription: Subscription) {
        subscriptionDao.deleteSubscription(subscription)
        
        // Sync to Firebase Firestore asynchronously
        scope.launch {
            try {
                firestore.collection("subscriptions")
                    .document(subscription.id.toString())
                    .delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun syncWithFirebase(onComplete: (Boolean) -> Unit) {
        firestore.collection("subscriptions")
            .get()
            .addOnSuccessListener { snapshot ->
                scope.launch {
                    try {
                        val remoteSubscriptions = snapshot.toObjects(Subscription::class.java)
                        for (subscription in remoteSubscriptions) {
                            subscriptionDao.insertSubscription(subscription)
                        }
                        onComplete(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onComplete(false)
                    }
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}
