@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.div05.comicsbox.ui.home

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.div05.comicsbox.R
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.model.PublicationsFeed
import com.div05.comicsbox.ui.components.ComicsBoxSnackbarHost
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun HomeWithPublicationDetailsScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onSelectPost: (String) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithList: () -> Unit,
    onInteractWithDetail: (String) -> Unit,
    openDrawer: () -> Unit,
    homeListLazyListState: LazyListState,
    publicationDetailLazyListStates: Map<String, LazyListState>,
    snackbarHostState: SnackbarHostState
) {
    TODO("Not yet implemented")
}


/**
 * The home screen displaying just the article feed.
 */
@Composable
fun HomeFeedScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onSelectPublication: (String) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    openDrawer: () -> Unit,
    homeListLazyListState: LazyListState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    HomeScreenWithList(
        uiState = uiState,
        showTopAppBar = showTopAppBar,
        onRefreshPosts = onRefreshPosts,
        onErrorDismiss = onErrorDismiss,
        openDrawer = openDrawer,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) { hasPostsUiState, contentPadding, contentModifier ->
        PublicationsListWrapper(
            publicationsFeed = hasPostsUiState.publicationsFeed,
            onPublicationTapped = onSelectPublication,
            contentPadding = contentPadding,
            modifier = contentModifier,
            state = homeListLazyListState,
        )
    }
}

/**
 * A display of the home screen that has the list.
 *
 * This sets up the scaffold with the top app bar, and surrounds the [hasPublicationsContent] with refresh,
 * loading and error handling.
 *
 * This helper functions exists because [HomeFeedWithArticleDetailsScreen] and [HomeFeedScreen] are
 * extremely similar, except for the rendered content when there are posts to display.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenWithList(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    hasPublicationsContent: @Composable (
        uiState: HomeUiState.HasPublications,
        contentPadding: PaddingValues,
        modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        snackbarHost = { ComicsBoxSnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (showTopAppBar) {
                HomeTopAppBar(
                    openDrawer = openDrawer,
                    topAppBarState = topAppBarState
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

        LoadingContent(
            empty = when (uiState) {
                is HomeUiState.HasPublications -> false
                is HomeUiState.NoPublications -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshPosts,
            content = {
                when (uiState) {
                    is HomeUiState.HasPublications ->
                        hasPublicationsContent(uiState, innerPadding, contentModifier)

                    is HomeUiState.NoPublications -> {
                        if (uiState.errorMessages.isEmpty()) {
                            // if there are no posts, and no error, let the user refresh manually
                            TextButton(
                                onClick = onRefreshPosts,
                                modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                Text(
                                    "Tap to load content",
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // there's currently an error showing, don't show any content
                            Box(
                                contentModifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) { /* empty screen */ }
                        }
                    }
                }
            }
        )
    }

    // Process one error message at a time and show them as Snackbars in the UI
    if (uiState.errorMessages.isNotEmpty()) {
        // Remember the errorMessage to display on the screen
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }

        // Get the text to show on the message from resources
        val errorMessageText: String = errorMessage.message
        val retryMessageText = "Retry"

        // If onRefreshPosts or onErrorDismiss change while the LaunchedEffect is running,
        // don't restart the effect and use the latest lambda values.
        val onRefreshPostsState by rememberUpdatedState(onRefreshPosts)
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        // Effect running in a coroutine that displays the Snackbar on the screen
        // If there's a change to errorMessageText, retryMessageText or snackbarHostState,
        // the previous effect will be cancelled and a new one will start with the new values
        LaunchedEffect(errorMessageText, retryMessageText, snackbarHostState) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorMessageText,
                actionLabel = retryMessageText
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                onRefreshPostsState()
            }
            // Once the message is displayed and dismissed, notify the ViewModel
            onErrorDismissState(errorMessage.id)
        }
    }
}

/**
 * Display a feed of publications.
 *
 * When a publication is clicked on, [onPublicationTapped] will be called.
 *
 * @param publicationsFeed (state) the feed to display
 * @param onPublicationTapped (event) request navigation to Article screen
 * @param modifier modifier for the root element
 */
@Composable
private fun PublicationsListWrapper(
    publicationsFeed: PublicationsFeed,
    onPublicationTapped: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state
    ) {
        if (publicationsFeed.publications.isNotEmpty()) {
            item {
                PublicationsList(
                    publicationsFeed.publications,
                    onPublicationTapped,
                )
            }
        }
    }
}

/**
 * Full-width list items for [PublicationsList]
 *
 * @param publications (state) to display
 * @param navigateToPublication (event) request navigation to Article screen
 */
@Composable
private fun PublicationsList(
    publications: List<Publication>,
    navigateToPublication: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        publications.forEach { publication ->
            PublicationCard(
                publication = publication,
                navigateToPublication = navigateToPublication,
            )
            PublicationListDivider()
        }
    }
}

@Composable
fun PublicationsV1(publications: List<Publication>, navigateToPublication: (String) -> Unit) {
    LazyColumn {
        items(publications) { publication ->
            PublicationCard(
                publication = publication,
                navigateToPublication = navigateToPublication
            )
            PublicationListDivider()
        }
    }
}

@Composable
fun PublicationCard(publication: Publication, navigateToPublication: (String) -> Unit) {
    Row(modifier = Modifier
        .clickable { navigateToPublication(publication.id) }
        .padding(all = 8.dp)) {

        AsyncImage(
            model = publication.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(80.dp)
                .fillMaxWidth()
                .clip(RectangleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, RectangleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
//        val surfaceColor by animateColorAsState(
//            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
//        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Surface(
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = publication.title,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                )
            }
        }

        PublicationListDivider()
    }
}

@Composable
fun PublicationListDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

/**
 * A divider for the list of posts.
 */
@Composable
fun HorizontalSpacer(modifier: Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(start = 72.dp, top = 8.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
    val context = LocalContext.current
    val title = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(R.drawable.ic_app_logo),
                contentDescription = title,
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(onClick = {
                Toast.makeText(
                    context,
                    "Search is not yet implemented in this configuration",
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.cd_search)
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

//==================================================================================================

/**
 * Display an initial empty state or swipe to refresh content.
 *
 * @param empty (state) when true, display [emptyContent]
 * @param emptyContent (slot) the content to display for the empty state
 * @param loading (state) when true, display a loading spinner over [content]
 * @param onRefresh (event) event to request refresh
 * @param content (slot) the main content to show
 */
@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content,
        )
    }
}

/**
 * Full screen circular progress indicator
 */
@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

