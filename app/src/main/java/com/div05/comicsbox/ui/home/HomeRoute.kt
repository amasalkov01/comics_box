package com.div05.comicsbox.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.div05.comicsbox.ui.publication.PublicationScreen

/**
 * Displays the Home route.
 *
 * Note: AAC ViewModels don't work with Compose Previews currently.
 *
 * @param homeViewModel ViewModel that handles the business logic of this screen
 * @param isExpandedScreen (state) whether the screen is expanded
 * @param openDrawer (event) request opening the app drawer
 * @param snackbarHostState (state) state for the [Scaffold] component on this screen
 */
@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        isExpandedScreen = isExpandedScreen,
        onSelectPost = { homeViewModel.selectPublication(it) },
        onRefreshPosts = { homeViewModel.refreshPosts() },
        onErrorDismiss = { homeViewModel.errorShown(it) },
        onInteractWithFeed = { homeViewModel.interactedWithFeed() },
        onInteractWithArticleDetails = { homeViewModel.interactedWithPublicationDetails(it) },
        openDrawer = openDrawer,
        snackbarHostState = snackbarHostState,
    )
}

/**
 * Displays the Home route.
 *
 * This composable is not coupled to any specific state management.
 *
 * @param uiState (state) the data to show on the screen
 * @param isExpandedScreen (state) whether the screen is expanded
 * @param onToggleFavorite (event) toggles favorite for a post
 * @param onSelectPost (event) indicate that a post was selected
 * @param onRefreshPosts (event) request a refresh of posts
 * @param onErrorDismiss (event) error message was shown
 * @param onInteractWithFeed (event) indicate that the feed was interacted with
 * @param onInteractWithArticleDetails (event) indicate that the article details were interacted
 * with
 * @param openDrawer (event) request opening the app drawer
 * @param snackbarHostState (state) state for the [Scaffold] component on this screen
 */
@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    onSelectPost: (String) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithFeed: () -> Unit,
    onInteractWithArticleDetails: (String) -> Unit,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    // Construct the lazy list states for the list and the details outside of deciding which one to
    // show. This allows the associated state to survive beyond that decision, and therefore
    // we get to preserve the scroll throughout any changes to the content.
    val homeListLazyListState = rememberLazyListState()
    val publicationDetailLazyListStates = when (uiState) {
        is HomeUiState.HasPublications -> uiState.publicationsFeed.allPublications
        is HomeUiState.NoPublications -> emptyList()
    }.associate { publication ->
        key(publication.id) {
            publication.id to rememberLazyListState()
        }
    }

    val homeScreenType = getHomeScreenType(isExpandedScreen, uiState)
    when (homeScreenType) {
        HomeScreenType.FeedWithArticleDetails -> {
            HomeWithPublicationDetailsScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreen,
                onSelectPost = onSelectPost,
                onRefreshPosts = onRefreshPosts,
                onErrorDismiss = onErrorDismiss,
                onInteractWithList = onInteractWithFeed,
                onInteractWithDetail = onInteractWithArticleDetails,
                openDrawer = openDrawer,
                homeListLazyListState = homeListLazyListState,
                publicationDetailLazyListStates = publicationDetailLazyListStates,
                snackbarHostState = snackbarHostState,
            )
        }
        HomeScreenType.Feed -> {
            HomeFeedScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreen,
                onSelectPublication = onSelectPost,
                onRefreshPosts = onRefreshPosts,
                onErrorDismiss = onErrorDismiss,
                openDrawer = openDrawer,
                homeListLazyListState = homeListLazyListState,
                snackbarHostState = snackbarHostState,
            )
        }
        HomeScreenType.ArticleDetails -> {
            // Guaranteed by above condition for home screen type
            check(uiState is HomeUiState.HasPublications)

            PublicationScreen(
                publication = uiState.selectedPublication,
                isExpandedScreen = isExpandedScreen,
                onBack = onInteractWithFeed,
                lazyListState = publicationDetailLazyListStates.getValue(
                    uiState.selectedPublication.id
                )
            )

            // If we are just showing the detail, have a back press switch to the list.
            // This doesn't take anything more than notifying that we "interacted with the list"
            // since that is what drives the display of the feed
            BackHandler {
                onInteractWithFeed()
            }
        }
    }
}


/**
 * A precise enumeration of which type of screen to display at the home route.
 *
 * There are 3 options:
 * - [FeedWithArticleDetails], which displays both a list of all articles and a specific article.
 * - [Feed], which displays just the list of all articles
 * - [ArticleDetails], which displays just a specific article.
 */
private enum class HomeScreenType {
    FeedWithArticleDetails,
    Feed,
    ArticleDetails
}

/**
 * Returns the current [HomeScreenType] to display, based on whether or not the screen is expanded
 * and the [HomeUiState].
 */
@Composable
private fun getHomeScreenType(
    isExpandedScreen: Boolean,
    uiState: HomeUiState
): HomeScreenType = when (isExpandedScreen) {
    false -> {
        when (uiState) {
            is HomeUiState.HasPublications -> {
                if (uiState.isPublicationOpen) {
                    HomeScreenType.ArticleDetails
                } else {
                    HomeScreenType.Feed
                }
            }
            is HomeUiState.NoPublications -> HomeScreenType.Feed
        }
    }
    true -> HomeScreenType.FeedWithArticleDetails
}