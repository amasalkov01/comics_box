package com.div05.comicsbox

import android.content.Context
import com.div05.comicsbox.ComicsBoxApplication.Companion.HTTP_CASH_SIZE_BYTES
import com.div05.comicsbox.data.feed.PublicationsRepository
import com.div05.comicsbox.data.feed.PublicationsTestRepository
import com.div05.comicsbox.data.feed.PublicationsWebRepository
import com.div05.comicsbox.data.mylib.MyLibraryRepository
import com.div05.comicsbox.data.mylib.MyLibraryTestDataRepository
import okhttp3.Cache
import okhttp3.OkHttpClient

interface AppContainer {
    val publicationsRepository: PublicationsRepository
    val myLibraryRepository: MyLibraryRepository
    val httpClient: OkHttpClient
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    override val httpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .cache(
                Cache(
                    directory = applicationContext.cacheDir,
                    maxSize = HTTP_CASH_SIZE_BYTES
                )
            )
            .build()

    override val publicationsRepository: PublicationsRepository by lazy {
        PublicationsWebRepository(httpClient)
//        PublicationsTestRepository()
    }

    override val myLibraryRepository: MyLibraryRepository by lazy {
        MyLibraryTestDataRepository()
    }

}

