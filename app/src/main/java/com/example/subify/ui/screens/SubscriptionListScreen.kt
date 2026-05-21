package com.example.subify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import com.example.subify.AddEditRoute
import com.example.subify.SubscriptionListRoute
import com.example.subify.data.model.BillingCycle
import com.example.subify.data.model.Subscription
import com.example.subify.theme.DarkBackground
import com.example.subify.theme.NeonCyan
import com.example.subify.theme.NeonPurple
import com.example.subify.theme.TextPrimary
import com.example.subify.theme.TextSecondary
import com.example.subify.ui.components.GlassCard
import com.example.subify.ui.list.SubscriptionListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SubscriptionListScreen(
    onNavigate: (NavKey) -> Unit,
    viewModel: SubscriptionListViewModel = hiltViewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            GlassNavigationBar(
                currentRoute = SubscriptionListRoute,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header
            Text(
                text = "My Subscriptions",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Manage active and recurring payments",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (subscriptions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You don't have any subscriptions yet.",
                        color = TextSecondary,
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(subscriptions) { subscription ->
                        SubscriptionListItem(
                            subscription = subscription,
                            onEdit = { onNavigate(AddEditRoute(subscription.id)) },
                            onDelete = { viewModel.deleteSubscription(subscription) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionListItem(
    subscription: Subscription,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val now = System.currentTimeMillis()
    val oneDayMillis = 24 * 60 * 60 * 1000L
    val dateString = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(subscription.nextPaymentDate))
    
    val daysRemaining = ((subscription.nextPaymentDate - now) / oneDayMillis).toInt().coerceAtLeast(0)
    val cycleLength = when (subscription.billingCycle) {
        BillingCycle.WEEKLY -> 7
        BillingCycle.MONTHLY -> 30
        BillingCycle.YEARLY -> 365
    }
    
    val daysElapsed = (cycleLength - daysRemaining).coerceIn(0, cycleLength)
    val progress = daysElapsed.toFloat() / cycleLength.toFloat()

    val cardIndicatorColor = Color(android.graphics.Color.parseColor(subscription.colorHex))

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Category indicator dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(cardIndicatorColor)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = subscription.name,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subscription.category,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${subscription.cost}${subscription.currency}",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar representing billing cycle
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = cardIndicatorColor,
                trackColor = Color.White.copy(alpha = 0.08f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Next payment: $dateString",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = if (daysRemaining == 0) "Due Today" else "$daysRemaining days left",
                    color = if (daysRemaining <= 2) NeonCyan else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
