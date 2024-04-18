package com.div05.comicsbox.data.feed

import com.div05.comicsbox.data.Operation
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.model.PublicationsFeed
import kotlinx.coroutines.flow.Flow

/**
 * Interface into data layer for feed
 */
interface PublicationsRepository {
    /**
     * Get Publication.
     */
    suspend fun getPublication(postId: String?): Operation<Publication>

    /**
     * Get Publications.
     */
    suspend fun getPublicationsFeed(): Operation<PublicationsFeed>

    /**
     * Observe the Publications feed.
     */
    suspend fun observePublicationsFeed(): Flow<PublicationsFeed?>

}