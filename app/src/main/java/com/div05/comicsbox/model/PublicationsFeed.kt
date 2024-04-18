package com.div05.comicsbox.model

data class PublicationsFeed(
    val highlightedPublication: Publication,
    val publications: List<Publication> = emptyList(),
    val attribution: Attribution = Attribution("", "")
) {
    val allPublications: List<Publication>
        get() = listOf(highlightedPublication) + publications
}