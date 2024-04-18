package com.div05.comicsbox.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.div05.comicsbox.AppContainer
import com.div05.comicsbox.ui.navigation.AppNavRail
import com.div05.comicsbox.ui.navigation.AppNavigationActions
import com.div05.comicsbox.ui.navigation.AppNavigationGraph
import com.div05.comicsbox.ui.navigation.Destinations
import com.div05.comicsbox.ui.theme.ComicsBoxTheme
import kotlinx.coroutines.launch

@Composable
fun ComicsBoxApp(
    appContainer: AppContainer,
    widthSizeClass: WindowWidthSizeClass
) {
    ComicsBoxTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            AppNavigationActions(navController)
        }
        val coroutineScope = rememberCoroutineScope()
        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: Destinations.HOME_ROUTE
        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)

        ModalNavigationDrawer(
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToHome = navigationActions.navigateToHome,
                    navigateToMyLibrary = navigationActions.navigateToMyLibrary,
                    closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } }
                )
            },
            drawerState = sizeAwareDrawerState,
            gesturesEnabled = !isExpandedScreen
        ) {
            Row {
                if (isExpandedScreen) {
                    AppNavRail(
                        currentRoute = currentRoute,
                        navigateToHome = navigationActions.navigateToHome,
                        navigateToMyLibrary = navigationActions.navigateToMyLibrary
                    )
                }
                AppNavigationGraph(
                    navController = navController,
                    appContainer = appContainer,
                    isExpandedScreen = isExpandedScreen,
                    openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } },
                )
            }
        }


    }
}

/**
 * Determine the drawer state to pass to the modal drawer.
 */
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}
