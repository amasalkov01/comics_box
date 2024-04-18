package com.div05.comicsbox.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


object Destinations {
    const val HOME_ROUTE = "home"
    const val MY_LIBRARY_ROUTE = "my_library"
}

class AppNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Destinations.HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToMyLibrary: () -> Unit = {
        navController.navigate(Destinations.MY_LIBRARY_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}