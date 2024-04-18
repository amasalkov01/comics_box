package com.div05.comicsbox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.div05.comicsbox.data.ErrorMessage
import com.div05.comicsbox.data.Operation
import com.div05.comicsbox.data.feed.PublicationsRepository
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.model.PublicationsFeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


class HomeViewModel(private val publicationsRepository: PublicationsRepository,
                    private val preSelectedPublicationId: String? = null
): ViewModel() {

    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true,
            selectedPublicationId = preSelectedPublicationId,
            isPublicationOpen = preSelectedPublicationId != null
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        refreshPosts()
    }

    fun refreshPosts() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = publicationsRepository.getPublicationsFeed()
            viewModelState.update {
                when (result) {
                    is Operation.Success -> it.copy(publicationsFeed = result.data, isLoading = false)
                    is Operation.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            message = "Failed to load publications"
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    fun selectPublication(publicationId: String) {
        // Treat selecting a detail as simply interacting with it
        interactedWithPublicationDetails(publicationId)
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun interactedWithFeed() {
        viewModelState.update {
            it.copy(isPublicationOpen = false)
        }
    }

    fun interactedWithPublicationDetails(publicationId: String) {
        viewModelState.update {
            it.copy(
                selectedPublicationId = publicationId,
                isPublicationOpen = true
            )
        }
    }

    companion object {
        fun provideFactory(
            publicationsRepository: PublicationsRepository,
            preSelectedPublicationId: String? = null
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(publicationsRepository, preSelectedPublicationId) as T
            }
        }
    }
}

private data class HomeViewModelState(
    val publicationsFeed: PublicationsFeed? = null,
    val selectedPublicationId: String? = null,
    val isPublicationOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
) {
    fun toUiState(): HomeUiState =
        if (publicationsFeed == null) {
            HomeUiState.NoPublications(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        } else {
            HomeUiState.HasPublications(
                publicationsFeed = publicationsFeed,
                // Determine the selected post. This will be the post the user last selected.
                // If there is none (or that post isn't in the current feed), default to the
                // highlighted post
                selectedPublication = publicationsFeed.publications.find {
                    it.id == selectedPublicationId
                } ?: publicationsFeed.highlightedPublication,
                isPublicationOpen = isPublicationOpen,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        }
}

sealed interface HomeUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val searchInput: String

    /**
     * There are no posts to render.
     *
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class NoPublications(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState

    /**
     * There are posts to render, as contained in [publicationsFeed].
     *
     * There is guaranteed to be a [selectedPublication], which is one of the posts from [publicationsFeed].
     */
    data class HasPublications(
        val publicationsFeed: PublicationsFeed,
        val selectedPublication: Publication,
        val isPublicationOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState
}

