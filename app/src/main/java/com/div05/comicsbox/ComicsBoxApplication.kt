package com.div05.comicsbox

import android.app.Application

class ComicsBoxApplication: Application() {
    companion object {
        const val COMICS_BOX_APP_URL = "https://github.com/amasalkov01/marvel_demo"

        // Marvel Comics API developer key
        const val API_KEY_MARVEL_PUBLIC = BuildConfig.API_KEY_MARVEL_PUBLIC
        const val API_KEY_MARVEL_PRIVATE = BuildConfig.API_KEY_MARVEL_PRIVATE
        const val HTTP_CASH_SIZE_BYTES = 10L * 1024L * 1024L // 10 MiB
    }

    // Dependency Injection for the application
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}