package com.example.subify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import com.example.subify.SettingsRoute
import com.example.subify.theme.DarkBackground
import com.example.subify.theme.NeonCyan
import com.example.subify.theme.NeonPurple
import com.example.subify.theme.TextPrimary
import com.example.subify.theme.TextSecondary
import com.example.subify.ui.components.GlassButton
import com.example.subify.ui.components.GlassCard
import com.example.subify.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    onNavigate: (NavKey) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            GlassNavigationBar(
                currentRoute = SettingsRoute,
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
                text = "Settings",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "App preferences and cloud synchronization",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Reminders toggle Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Payment Reminders",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Get daily notifications for upcoming subscriptions 1-2 days before they renew.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = viewModel.notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextPrimary,
                            checkedTrackColor = NeonPurple,
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.08f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Firebase Cloud sync Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column {
                        Text(
                            text = "Firebase Cloud Sync",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Back up and sync your local subscription records securely to your Firebase Firestore cloud database.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    if (viewModel.isSyncing) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Syncing...", color = NeonCyan, fontSize = 14.sp)
                        }
                    } else {
                        GlassButton(
                            onClick = { viewModel.triggerCloudSync() },
                            text = "Sync with Cloud"
                        )
                    }

                    if (viewModel.syncMessage.isNotEmpty()) {
                        Text(
                            text = viewModel.syncMessage,
                            color = if (viewModel.syncMessage.contains("successfully")) NeonCyan else NeonPurple,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}
