package com.example.subify

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.subify.ui.screens.AddEditScreen
import com.example.subify.ui.screens.DashboardScreen
import com.example.subify.ui.screens.SettingsScreen
import com.example.subify.ui.screens.SubscriptionListScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(DashboardRoute)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<DashboardRoute> {
          DashboardScreen(
            onNavigate = { route -> backStack.add(route) }
          )
        }
        entry<SubscriptionListRoute> {
          SubscriptionListScreen(
            onNavigate = { route -> backStack.add(route) }
          )
        }
        entry<SettingsRoute> {
          SettingsScreen(
            onNavigate = { route -> backStack.add(route) }
          )
        }
        entry<AddEditRoute> { route ->
          AddEditScreen(
            subscriptionId = route.id,
            onBack = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}
