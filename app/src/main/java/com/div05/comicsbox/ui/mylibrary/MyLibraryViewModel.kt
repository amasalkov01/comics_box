package com.div05.comicsbox.ui.mylibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.div05.comicsbox.data.mylib.MyLibraryRepository

class MyLibraryViewModel(
    val myLibraryDataRepository: MyLibraryRepository
): ViewModel() {

    companion object {
        fun provideFactory(
            myLibraryRepositiry: MyLibraryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyLibraryViewModel(myLibraryRepositiry) as T
            }
        }
    }
}
