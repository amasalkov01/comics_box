package com.div05.comicsbox.ui

import com.div05.comicsbox.ComicsBoxApplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as ComicsBoxApplication).container

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            ComicsBoxApp(appContainer, widthSizeClass)
        }
    }
}
