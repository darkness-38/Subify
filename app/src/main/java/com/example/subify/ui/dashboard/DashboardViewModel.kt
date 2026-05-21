package com.example.subify.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subify.data.model.BillingCycle
import com.example.subify.data.model.Subscription
import com.example.subify.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: SubscriptionRepository
) : ViewModel() {

    val subscriptions: StateFlow<List<Subscription>> = repository.getAllSubscriptions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalMonthlySpending: StateFlow<Double> = subscriptions.map { list ->
        list.sumOf { sub ->
            when (sub.billingCycle) {
                BillingCycle.WEEKLY -> sub.cost * 4.33
                BillingCycle.MONTHLY -> sub.cost
                BillingCycle.YEARLY -> sub.cost / 12.0
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val categorySpending: StateFlow<Map<String, Double>> = subscriptions.map { list ->
        list.groupBy { it.category }
            .mapValues { (_, subs) ->
                subs.sumOf { sub ->
                    when (sub.billingCycle) {
                        BillingCycle.WEEKLY -> sub.cost * 4.33
                        BillingCycle.MONTHLY -> sub.cost
                        BillingCycle.YEARLY -> sub.cost / 12.0
                    }
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    val upcomingPayments: StateFlow<List<Subscription>> = subscriptions.map { list ->
        val now = System.currentTimeMillis()
        val sevenDaysMillis = 7 * 24 * 60 * 60 * 1000L
        list.filter { sub ->
            sub.nextPaymentDate in now..(now + sevenDaysMillis)
        }.sortedBy { it.nextPaymentDate }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
