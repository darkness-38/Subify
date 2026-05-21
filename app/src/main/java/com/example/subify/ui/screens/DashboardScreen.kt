package com.example.subify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import com.example.subify.AddEditRoute
import com.example.subify.DashboardRoute
import com.example.subify.SettingsRoute
import com.example.subify.SubscriptionListRoute
import com.example.subify.theme.DarkBackground
import com.example.subify.theme.DeepDarkNavy
import com.example.subify.theme.NeonCyan
import com.example.subify.theme.NeonPurple
import com.example.subify.theme.TextPrimary
import com.example.subify.theme.TextSecondary
import com.example.subify.ui.components.GlassCard
import com.example.subify.ui.components.NeonCategoryChart
import com.example.subify.ui.dashboard.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigate: (NavKey) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val totalSpending by viewModel.totalMonthlySpending.collectAsState()
    val categoryData by viewModel.categorySpending.collectAsState()
    val upcomingList by viewModel.upcomingPayments.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(AddEditRoute(0)) },
                containerColor = Color.Transparent,
                contentColor = TextPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(NeonPurple, NeonCyan)))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Subscription", modifier = Modifier.size(28.dp))
            }
        },
        bottomBar = {
            GlassNavigationBar(
                currentRoute = DashboardRoute,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Subify",
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Budget & subscription control",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Screen 1: Top Section - Large Glassmorphism Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "TOTAL MONTHLY SPENDING",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = String.format("₺%,.2f", totalSpending),
                        color = TextPrimary,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Screen 1: Middle Section - Categories Chart
            Text(
                text = "EXPENSES BY CATEGORY",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (categoryData.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(168.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No subscription data yet.", color = TextSecondary, fontSize = 14.sp)
                        }
                    } else {
                        NeonCategoryChart(data = categoryData)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Screen 1: Bottom Section - Upcoming Payments
            Text(
                text = "UPCOMING PAYMENTS (7 DAYS)",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (upcomingList.isEmpty()) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No payments due in the next 7 days.", color = TextSecondary, fontSize = 14.sp)
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    for (sub in upcomingList) {
                        UpcomingPaymentCard(subscription = sub, onClick = { onNavigate(AddEditRoute(sub.id)) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun UpcomingPaymentCard(
    subscription: com.example.subify.data.model.Subscription,
    onClick: () -> Unit
) {
    val dateString = SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(subscription.nextPaymentDate))
    val remainingDays = ((subscription.nextPaymentDate - System.currentTimeMillis()) / (24 * 60 * 60 * 1000L)).toInt().coerceAtLeast(0)

    val remainingLabel = when (remainingDays) {
        0 -> "Today"
        1 -> "Tomorrow"
        else -> "In $remainingDays days"
    }

    GlassCard(
        modifier = Modifier
            .width(180.dp)
            .height(100.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Colored Indicator Dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(subscription.colorHex)))
                )
                Text(
                    text = remainingLabel,
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column {
                Text(
                    text = subscription.name,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${subscription.cost}${subscription.currency} • $dateString",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun GlassNavigationBar(
    currentRoute: NavKey,
    onNavigate: (NavKey) -> Unit
) {
    val glassGradient = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.08f),
            Color.White.copy(alpha = 0.02f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(glassGradient)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.20f),
                        Color.White.copy(alpha = 0.04f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home / Dashboard
            IconButton(
                icon = Icons.Default.Home,
                label = "Home",
                isActive = currentRoute == DashboardRoute,
                onClick = { onNavigate(DashboardRoute) }
            )

            // Subscriptions List
            IconButton(
                icon = Icons.Default.List,
                label = "List",
                isActive = currentRoute == SubscriptionListRoute,
                onClick = { onNavigate(SubscriptionListRoute) }
            )

            // Settings
            IconButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                isActive = currentRoute == SettingsRoute,
                onClick = { onNavigate(SettingsRoute) }
            )
        }
    }
}

@Composable
fun IconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val activeColor = NeonCyan
    val inactiveColor = TextSecondary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) activeColor else inactiveColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = if (isActive) activeColor else inactiveColor,
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}
