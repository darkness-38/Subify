package com.example.subify.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.subify.data.local.SubifyDatabase
import com.example.subify.data.model.Subscription

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val now = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000L
        val twoDaysMillis = 2 * oneDayMillis

        // Access the database inside the worker safely
        val db = Room.databaseBuilder(
            applicationContext,
            SubifyDatabase::class.java,
            "subify_database"
        ).build()
        
        val dao = db.subscriptionDao()
        
        // Retrieve renewals 1 to 2 days away (e.g. up to 48 hours from now)
        val upcomingSubscriptions = dao.getUpcomingSubscriptions(now, now + twoDaysMillis)

        for (sub in upcomingSubscriptions) {
            val diff = sub.nextPaymentDate - now
            val daysLeft = (diff / oneDayMillis).toInt().coerceInText(1, 2)
            val timeLabel = if (daysLeft == 1) "tomorrow" else "in $daysLeft days"
            
            val message = "Heads up! Your ${sub.name} subscription of ${sub.cost}${sub.currency} is renewing $timeLabel. Ensure your digital wallet/card has sufficient funds."
            sendNotification(sub.id, sub.name, message)
        }

        db.close()
        return Result.success()
    }

    private fun Int.coerceInText(min: Int, max: Int): Int {
        return if (this < min) min else if (this > max) max else this
    }

    private fun sendNotification(id: Int, title: String, message: String) {
        val channelId = "subify_payments_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Payment Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for upcoming digital subscription renewals"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // System default icon is guaranteed to exist and render beautifully
            .setContentTitle("Subscription Renewal: $title")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(id, builder.build())
    }
}
