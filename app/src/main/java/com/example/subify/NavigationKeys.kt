package com.example.subify

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object DashboardRoute : NavKey
@Serializable data object SubscriptionListRoute : NavKey
@Serializable data object SettingsRoute : NavKey
@Serializable data class AddEditRoute(val id: Int = 0) : NavKey
