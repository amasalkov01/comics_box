package com.div05.comicsbox.ui.publication

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.div05.comicsbox.R
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.ui.home.PublicationListDivider
import com.div05.comicsbox.ui.home.SelectTopicButton
import com.div05.comicsbox.ui.theme.ReadButtonColor

@Composable
fun PublicationScreen(
    publication: Publication,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState
) {
    var showUnimplementedActionDialog by rememberSaveable { mutableStateOf(false) }
    if (showUnimplementedActionDialog) {
        FunctionalityNotAvailablePopup { showUnimplementedActionDialog = false }
    }

    Row(modifier.fillMaxSize()) {
        val context = LocalContext.current
        PublicationScreenContent(
            publication = publication,
            // Allow opening the Drawer if the screen is not expanded
            navigationIconContent = {
                if (!isExpandedScreen) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate up",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            lazyListState = lazyListState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicationScreenContent(publication: Publication,
                             navigationIconContent: @Composable () -> Unit = { },
                             bottomBarContent: @Composable () -> Unit = { },
                             lazyListState: LazyListState = rememberLazyListState()
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Scaffold(
        topBar = {
            TopAppBar(
                title = publication.title,
                navigationIconContent = navigationIconContent,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = bottomBarContent
    ) { innerPadding ->
        PostContent(
            publication = publication,
            contentPadding = innerPadding,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = lazyListState,
            )
    }
}

private val defaultSpacerSize = 16.dp

@Composable
fun PostContent(
    publication: Publication,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.padding(horizontal = defaultSpacerSize),
        state = state,
    ) {
        item{PublicationDetails(publication, modifier)}
    }
}

@Composable
fun PublicationDetails(
    publication: Publication,
    modifier: Modifier = Modifier,
) {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Row {
            // Publication header
            PublicationHeader(publication)
            PublicationListDivider()
        }

        Row(modifier = Modifier.padding(all = 8.dp)) {
            // Publication's Title
            Text(
                text = publication.title,
                modifier = modifier
            )

            PublicationListDivider()
        }

        var isExpanded by remember { mutableStateOf(false) }
        Column(modifier = Modifier.padding(all = 8.dp)) {
            Row {
                if (publication.description.isNotEmpty()) {
                    Text(
                        text = "The Story",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            VerticalDivider(modifier.size(1.dp, 1.dp))
            Row(modifier = Modifier
                .clickable { isExpanded = !isExpanded }) {
                Text(
                    text = publication.description,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 20,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun PublicationHeader(publication: Publication, modifier: Modifier = Modifier) {
    // painter to set background image
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = publication.thumbnail)
            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                transformations()
            }).build()
    )

    Row(modifier = Modifier.padding(all = 8.dp)) {
        AsyncImage(
            model = publication.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp, 180.dp)
                .clip(RectangleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, RectangleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            ReadButton(itemTitle = "READ NOW", selected = false, onToggle = { /*TODO*/ })
            ActionButton(itemTitle = "MARK AS READ", selected = false, onToggle = { /*TODO*/ })
            ActionButton(itemTitle = "ADD TO LIBRARY", selected = false, onToggle = { /*TODO*/ })
            ActionButton(itemTitle = "READ OFFLINE", selected = false, onToggle = { /*TODO*/ })
        }
    }
}

@Composable
private fun ActionButton(
    itemTitle: String,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(0.5.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier.toggleable(
                value = selected,
                onValueChange = { onToggle() }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectTopicButton(selected = selected)
            Spacer(Modifier.width(8.dp))
            Text(
                text = itemTitle,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f), // Break line if the title is too long
                style = MaterialTheme.typography.titleSmall
            )
        }
//        HorizontalDivider(
//            modifier = modifier.padding(start = 72.dp, top = 8.dp, bottom = 8.dp),
//            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
//        )
    }
}

//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Preview(showBackground = true)
@Composable
fun PreviewActionButton() {
    ActionButton(itemTitle = "DOWNLOAD", selected = false, onToggle = { /*TODO*/ })
}

@Composable
fun ReadButton(
    itemTitle: String,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .background(ReadButtonColor)
    ) {
        Row(
            modifier = modifier.toggleable(
                value = selected,
                onValueChange = { onToggle() }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = itemTitle,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f), // Break line if the title is too long
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Preview(showBackground = true)
@Composable
fun PreviewReadButton() {
    ReadButton(itemTitle = "READ NOW", selected = false, onToggle = { /*TODO*/ })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    title: String,
    navigationIconContent: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Row {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },
        navigationIcon = navigationIconContent,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

/**
 * Display a popup explaining functionality not available.
 *
 * @param onDismiss (event) request the popup be dismissed
 */
@Composable
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close")
            }
        }
    )
}
