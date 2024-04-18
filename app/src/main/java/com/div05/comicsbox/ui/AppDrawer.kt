package com.div05.comicsbox.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.div05.comicsbox.R
import com.div05.comicsbox.ui.navigation.Destinations

@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToMyLibrary: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {

    ModalDrawerSheet {
        ComicsBoxLogo(modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp))
        NavigationDrawerItem(
            selected = currentRoute == Destinations.HOME_ROUTE,
            onClick = { navigateToHome(); closeDrawer() },
            icon = { Icon(Icons.Filled.Home, "Home") },
            label = { Text("Home") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            selected = currentRoute == Destinations.MY_LIBRARY_ROUTE,
            onClick = { navigateToMyLibrary(); navigateToMyLibrary() },
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, "My Library") },
            label = { Text("My Library") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun ComicsBoxLogo(modifier: Modifier = Modifier) {
    Row {
        Icon(painter = painterResource(id = R.drawable.ic_app_logo), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    }
}

@Preview("Drawer preview")
@Preview("Drawer preview (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    AppDrawer(currentRoute = Destinations.HOME_ROUTE, navigateToHome = {}, navigateToMyLibrary = {}, closeDrawer = {})
}