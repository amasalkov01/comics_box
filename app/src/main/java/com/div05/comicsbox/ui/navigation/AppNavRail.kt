package com.div05.comicsbox.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.div05.comicsbox.R
import com.div05.comicsbox.ui.theme.ComicsBoxTheme

@Composable
fun AppNavRail(currentRoute: String,
               navigateToHome: () -> Unit,
               navigateToMyLibrary: () -> Unit,
               modifier: Modifier = Modifier
               ) {
    NavigationRail(
        header = {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = null,
                modifier = Modifier.padding(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(1f))

        NavigationRailItem(
            selected = currentRoute == Destinations.HOME_ROUTE,
            onClick = navigateToHome,
            icon = { Icon(Icons.Filled.Home, "Home") },
            label = { Text("Home") },
            alwaysShowLabel = false
        )

        NavigationRailItem(
            selected = currentRoute == Destinations.MY_LIBRARY_ROUTE,
            onClick = navigateToMyLibrary,
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, "My Library") },
            label = { Text("My Library") },
            alwaysShowLabel = false
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppNavRail() {
    ComicsBoxTheme {
        AppNavRail(
            currentRoute = Destinations.HOME_ROUTE,
            navigateToHome = {},
            navigateToMyLibrary = {},
        )
    }
}
