package com.div05.comicsbox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.div05.comicsbox.AppContainer
import com.div05.comicsbox.ComicsBoxApplication.Companion.COMICS_BOX_APP_URL
import com.div05.comicsbox.ui.home.HomeRoute
import com.div05.comicsbox.ui.home.HomeViewModel
import com.div05.comicsbox.ui.mylibrary.MyLibraryViewModel

object NavigationTargets {
    const val HOME_SCREEN = "home"
    const val MY_LIBRARY_SCREEN = "mylibrary"
}

const val TITLE_ID = "titleId"

@Composable
fun AppNavigationGraph(
    appContainer: AppContainer,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = NavigationTargets.HOME_SCREEN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = NavigationTargets.HOME_SCREEN,
            deepLinks = listOf(navDeepLink {
                uriPattern =
                    "$COMICS_BOX_APP_URL/${NavigationTargets.HOME_SCREEN}?$TITLE_ID={$TITLE_ID}"
            })
        ) { navBackStackEntry ->
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(
                    publicationsRepository = appContainer.publicationsRepository,
                    preSelectedPublicationId = navBackStackEntry.arguments?.getString(TITLE_ID)
                )
            )
            HomeRoute(
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
            )
        }
        composable(NavigationTargets.MY_LIBRARY_SCREEN) {
            val myLibraryViewModel: MyLibraryViewModel = viewModel(
                factory = MyLibraryViewModel.provideFactory(appContainer.myLibraryRepository)
            )
            // TODO MyLibraryRoute(
//            MyLibraryRoute(
//                interestsViewModel = myLibraryViewModel,
//                isExpandedScreen = isExpandedScreen,
//                openDrawer = openDrawer
//            )
        }
    }
}

