package com.example.subify.ui.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subify.data.model.BillingCycle
import com.example.subify.data.model.Subscription
import com.example.subify.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: SubscriptionRepository
) : ViewModel() {

    var subscriptionId by mutableStateOf(0)
        private set

    var name by mutableStateOf("")
    var cost by mutableStateOf("")
    var currency by mutableStateOf("₺")
    var billingCycle by mutableStateOf(BillingCycle.MONTHLY)
    var nextPaymentDate by mutableStateOf(System.currentTimeMillis())
    var category by mutableStateOf("Gaming")
    var colorHex by mutableStateOf("#FF1A75") // Default neon accent color

    var isEditMode by mutableStateOf(false)
        private set

    fun loadSubscription(id: Int) {
        if (id <= 0) return
        subscriptionId = id
        isEditMode = true
        viewModelScope.launch {
            repository.getSubscriptionById(id).firstOrNull()?.let { sub ->
                name = sub.name
                cost = sub.cost.toString()
                currency = sub.currency
                billingCycle = sub.billingCycle
                nextPaymentDate = sub.nextPaymentDate
                category = sub.category
                colorHex = sub.colorHex
            }
        }
    }

    fun saveSubscription(onSuccess: () -> Unit) {
        val costDouble = cost.toDoubleOrNull() ?: return
        val subscription = Subscription(
            id = subscriptionId,
            name = name,
            cost = costDouble,
            currency = currency,
            billingCycle = billingCycle,
            nextPaymentDate = nextPaymentDate,
            category = category,
            colorHex = colorHex
        )
        viewModelScope.launch {
            repository.insertSubscription(subscription)
            onSuccess()
        }
    }
}
