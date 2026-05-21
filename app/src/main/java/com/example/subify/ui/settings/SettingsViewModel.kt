package com.example.subify.ui.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.subify.data.repository.SubscriptionRepository
import com.example.subify.data.worker.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var notificationsEnabled by mutableStateOf(true)
    var isSyncing by mutableStateOf(false)
    var syncMessage by mutableStateOf("")

    private val workManager = WorkManager.getInstance(context)

    fun toggleNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
        if (enabled) {
            // Schedule daily periodic checks for subscription payments
            val notificationWork = PeriodicWorkRequestBuilder<NotificationWorker>(
                1, TimeUnit.DAYS
            ).build()
            workManager.enqueueUniquePeriodicWork(
                "subify_daily_notifications",
                ExistingPeriodicWorkPolicy.KEEP,
                notificationWork
            )
        } else {
            // Cancel background tasks
            workManager.cancelUniqueWork("subify_daily_notifications")
        }
    }

    fun triggerCloudSync() {
        isSyncing = true
        syncMessage = "Connecting to cloud..."
        repository.syncWithFirebase { success ->
            isSyncing = false
            syncMessage = if (success) {
                "Cloud sync completed successfully!"
            } else {
                "Failed to sync with cloud. Try again."
            }
        }
    }
}
