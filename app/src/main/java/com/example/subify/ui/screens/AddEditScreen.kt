package com.example.subify.ui.screens

import android.app.DatePickerDialog
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.subify.data.model.BillingCycle
import com.example.subify.theme.DarkBackground
import com.example.subify.theme.NeonCyan
import com.example.subify.theme.NeonEmerald
import com.example.subify.theme.NeonOrange
import com.example.subify.theme.NeonPink
import com.example.subify.theme.NeonPurple
import com.example.subify.theme.TextPrimary
import com.example.subify.theme.TextSecondary
import com.example.subify.ui.addedit.AddEditViewModel
import com.example.subify.ui.components.GlassButton
import com.example.subify.ui.components.GlassCard
import com.example.subify.ui.components.GlassTextField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddEditScreen(
    subscriptionId: Int,
    onBack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    
    // Load subscription if editing
    LaunchedEffect(subscriptionId) {
        if (subscriptionId > 0) {
            viewModel.loadSubscription(subscriptionId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (viewModel.isEditMode) "Edit Subscription" else "New Subscription",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Cards
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                // Subscription Name Input
                GlassTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = "Subscription Name (e.g. Steam Wallet)",
                    modifier = Modifier.fillMaxWidth()
                )

                // Cost & Currency Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    GlassTextField(
                        value = viewModel.cost,
                        onValueChange = { viewModel.cost = it },
                        label = "Cost",
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Currency Picker Box
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "CURRENCY", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("₺", "$", "€").forEach { curr ->
                                val isSelected = viewModel.currency == curr
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) NeonPurple.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.03f))
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) NeonPurple else Color.White.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.currency = curr },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = curr, color = if (isSelected) NeonPurple else TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Billing Cycle Selection
                Column {
                    Text(text = "BILLING CYCLE", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BillingCycle.values().forEach { cycle ->
                            val isSelected = viewModel.billingCycle == cycle
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.03f))
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) NeonCyan else Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.billingCycle = cycle },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cycle.name,
                                    color = if (isSelected) NeonCyan else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Date Picker Button
                Column {
                    Text(text = "NEXT PAYMENT DATE", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val formattedDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(viewModel.nextPaymentDate))
                    
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable {
                                val cal = Calendar.getInstance()
                                cal.timeInMillis = viewModel.nextPaymentDate
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        val selectedCal = Calendar.getInstance()
                                        selectedCal.set(year, month, day)
                                        viewModel.nextPaymentDate = selectedCal.timeInMillis
                                    },
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = formattedDate, color = TextPrimary, fontSize = 16.sp)
                            Text(text = "Select Date", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Category Selection
                Column {
                    Text(text = "CATEGORY", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("Gaming", "Entertainment", "Finance", "Utilities", "Other").forEach { cat ->
                            val isSelected = viewModel.category == cat
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) NeonPurple.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.03f))
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) NeonPurple else Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable { viewModel.category = cat }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) NeonPurple else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Color Hex Selection (Preset Dots)
                Column {
                    Text(text = "CARD INDICATOR COLOR", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOf(
                            "#FF007F" to NeonPink,
                            "#B82DFE" to NeonPurple,
                            "#00F0FF" to NeonCyan,
                            "#00FF87" to NeonEmerald,
                            "#FF5E00" to NeonOrange
                        ).forEach { (hex, col) ->
                            val isSelected = viewModel.colorHex.lowercase() == hex.lowercase()
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(col)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.colorHex = hex }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Save button
                GlassButton(
                    onClick = {
                        viewModel.saveSubscription {
                            onBack()
                        }
                    },
                    text = if (viewModel.isEditMode) "Save Changes" else "Create Subscription"
                )
            }
        }
    }
}
