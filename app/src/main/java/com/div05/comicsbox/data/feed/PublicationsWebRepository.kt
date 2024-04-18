package com.div05.comicsbox.data.feed

import android.net.Uri
import com.div05.comicsbox.ComicsBoxApplication.Companion.API_KEY_MARVEL_PRIVATE
import com.div05.comicsbox.ComicsBoxApplication.Companion.API_KEY_MARVEL_PUBLIC
import com.div05.comicsbox.data.Operation
import com.div05.comicsbox.model.Attribution
import com.div05.comicsbox.model.MarvelResponse
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.model.PublicationsFeed
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.security.MessageDigest

class PublicationsWebRepository(private val client:OkHttpClient): PublicationsRepository {

    private val publicationsFeed = MutableStateFlow<PublicationsFeed?>(null)

    private fun refreshFeed(): PublicationsFeed {
        val request = buildGetComicsRequest()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val marvelFeed = parseComicsRespondJson(response.body!!.string())

            if (marvelFeed?.data?.results?.isEmpty() == true) throw IOException("No results found")

            return buildPublicationsFeed(marvelFeed)
        }
    }

    override suspend fun getPublicationsFeed(): Operation<PublicationsFeed> {
        return withContext(Dispatchers.IO) {
            try {
                val results = refreshFeed()
                publicationsFeed.update { results }
                Operation.Success(results)
            } catch(e: Exception) {
                Operation.Error(IllegalStateException("Failed to load publications $e"))
            }
        }
    }

    override suspend fun getPublication(postId: String?): Operation<Publication> {
        return withContext(Dispatchers.IO) {
            val publication = publicationsFeed.value?.publications?.find { it.id == postId }
            if (publication == null) {
                Operation.Error(IllegalArgumentException("Post not found"))
            } else {
                Operation.Success(publication)
            }
        }
    }

    override suspend fun observePublicationsFeed(): Flow<PublicationsFeed?> = publicationsFeed

    companion object {
        const val TIMESTAMP = "ts"
        const val API_URL = "https://gateway.marvel.com:443/v1/public/comics"
        const val API_KEY = "apikey"
        const val MD5_HASH = "hash"
        const val FORMAT = "format"
        const val LIMIT = "limit"
    }
}

/***
 *  Authentication for Server-Side Applications
 *  Server-side applications must pass two parameters in addition to the apikey parameter:
 *
 *  ts - a timestamp (or other long string which can change on a request-by-request basis)
 *  hash - a md5 digest of the ts parameter, your private key and your public key (e.g. md5(ts+privateKey+publicKey)
 *  For example, a user with a public key of "1234" and a private key of "abcd" could construct a valid call as follows:
 *  http://gateway.marvel.com/v1/public/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150
 *  (the hash value is the md5 digest of 1abcd1234)
 *
 ***/
private fun PublicationsWebRepository.calculateHash(timestamp: String): String {
    val md = MessageDigest.getInstance("MD5")
    val signature = timestamp + API_KEY_MARVEL_PRIVATE + API_KEY_MARVEL_PUBLIC
    val hashBytes = md.digest(signature.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}

private fun PublicationsWebRepository.buildGetComicsRequest(format: String = "comic", limit: String = "20"): Request {
    val timestamp = timestamp
    val url = Uri.parse(PublicationsWebRepository.API_URL)
        .buildUpon()
        .appendQueryParameter(PublicationsWebRepository.FORMAT, format)
        .appendQueryParameter(PublicationsWebRepository.LIMIT, limit)
        .appendQueryParameter(PublicationsWebRepository.TIMESTAMP, timestamp)
        .appendQueryParameter(PublicationsWebRepository.API_KEY, API_KEY_MARVEL_PUBLIC)
        .appendQueryParameter(PublicationsWebRepository.MD5_HASH, calculateHash(timestamp))

    return Request.Builder()
        .url(url.build().toString())
        .build()
}

private val timestamp =
    System.currentTimeMillis().toString()

private fun PublicationsWebRepository.buildPublicationsFeed(marvelFeed: MarvelResponse?): PublicationsFeed {
    val publications = marvelFeed?.data?.results?.map { result ->
        Publication(
            id = result.id.toString(),
            title = result.title,
            description = result.textObjects.joinToString("\n") { it.text },
            thumbnail = result.thumbnail.path + "." + result.thumbnail.extension
        )
    }

    return PublicationsFeed(
        publications = publications!!,
        highlightedPublication = publications[0],
        attribution = Attribution(
            attributionText = marvelFeed.attributionText,
            attributionHTML = marvelFeed.attributionHTML
        )
    )
}

private fun PublicationsWebRepository.parseComicsRespondJson(jsonString: String): MarvelResponse? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(MarvelResponse::class.java)
    return jsonAdapter.fromJson(jsonString)
}
