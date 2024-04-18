package com.div05.comicsbox.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
* Data classes for publications
 */
data class Publication(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String
)

data class Attribution(
    val attributionText: String,
    val attributionHTML: String
)

/**
 * Marvel API response model
 */
@JsonClass(generateAdapter = true)
data class MarvelResponse(
    val code: Int,
    val status: String,
    val copyright: String,
    @Json(name = "attributionText") val attributionText: String,
    @Json(name = "attributionHTML") val attributionHTML: String,
    val etag: String,
    val data: Data
)

@JsonClass(generateAdapter = true)
data class Data(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Result>
)

@JsonClass(generateAdapter = true)
data class Result(
    val id: Int,
//    @Json(name = "digitalId") val digitalId: Int,
    val title: String,
//    @Json(name = "issueNumber") val issueNumber: Int,
//    @Json(name = "variantDescription") val variantDescription: String,
//    val description: String?,
//    val modified: String,
//    val isbn: String,
//    val upc: String,
//    @Json(name = "diamondCode") val diamondCode: String,
//    val ean: String,
//    val issn: String,
//    val format: String,
//    @Json(name = "pageCount") val pageCount: Int,
    val textObjects: List<TextObject>,
//    @Json(name = "textObjects") val textObjects: List<TextObject>,
//    @Json(name = "resourceURI") val resourceURI: String,
//    val urls: List<Url>,
//    val series: Series,
//    val variants: List<Any>,
//    val collections: List<Any>,
//    @Json(name = "collectedIssues") val collectedIssues: List<Any>,
//    val dates: List<Date>,
//    val prices: List<Price>,
    val thumbnail: Thumbnail,
//    val images: List<Image>,
//    val creators: Creators,
//    val characters: Characters,
//    val stories: Stories,
//    val events: Events
)

@JsonClass(generateAdapter = true)
data class TextObject(
    val type: String,
    val language: String,
    val text: String
)

@JsonClass(generateAdapter = true)
data class Url(
    val type: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class Series(
    @Json(name = "resourceURI") val resourceURI: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class Date(
    val type: String,
    val date: String
)

@JsonClass(generateAdapter = true)
data class Price(
    val type: String,
    val price: Double
)

@JsonClass(generateAdapter = true)
data class Thumbnail(
    val path: String,
    val extension: String
)

@JsonClass(generateAdapter = true)
data class Image(
    val path: String,
    val extension: String
)

@JsonClass(generateAdapter = true)
data class Creators(
    val available: Int,
    @Json(name = "collectionURI") val collectionURI: String,
    val items: List<Creator>,
    val returned: Int
)

@JsonClass(generateAdapter = true)
data class Creator(
    @Json(name = "resourceURI") val resourceURI: String,
    val name: String,
    val role: String
)

@JsonClass(generateAdapter = true)
data class Characters(
    val available: Int,
    @Json(name = "collectionURI") val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)

@JsonClass(generateAdapter = true)
data class Stories(
    val available: Int,
    @Json(name = "collectionURI") val collectionURI: String,
    val items: List<Story>,
    val returned: Int
)
//
@JsonClass(generateAdapter = true)
data class Story(
    @Json(name = "resourceURI") val resourceURI: String,
    val name: String,
    val type: String
)

@JsonClass(generateAdapter = true)
data class Events(
    val available: Int,
    @Json(name = "collectionURI") val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)
