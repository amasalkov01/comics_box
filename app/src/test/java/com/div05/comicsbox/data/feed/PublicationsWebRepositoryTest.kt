package com.div05.comicsbox.data.feed

import com.div05.comicsbox.data.feed.PublicationsWebRepository
import com.div05.comicsbox.model.Attribution
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.model.PublicationsFeed
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito

class PublicationsWebRepositoryTest {

    @Ignore("Mokito issue")
    @Test
    fun testGetPublicationsFeed() = runBlocking {
        // Mock OkHttpClient
        val client = Mockito.mock(OkHttpClient::class.java)
        val call = Mockito.mock(Call::class.java)
        val response = Mockito.mock(Response::class.java)
        val responseBody =
            ResponseBody.create("application/json".toMediaTypeOrNull(), marvelJsonString)

        Mockito.`when`(client.newCall(Mockito.any(Request::class.java))).thenReturn(call)
        Mockito.`when`(call.execute()).thenReturn(response)
        Mockito.`when`(response.isSuccessful).thenReturn(true)
        Mockito.`when`(response.body).thenReturn(responseBody)

        // Create PublicationsWebRepository
        val repository = PublicationsWebRepository(client)

        // Call getPublicationsFeed and check the result
        val result = repository.getPublicationsFeed()
        val expected = publicationsFeed

        assertEquals(expected, result)
    }
}

val publicationsFeed = PublicationsFeed(
    attribution = Attribution(
        attributionText = "Data provided by Marvel. © 2024 MARVEL",
        attributionHTML = "<a href=\"http://marvel.com\">Data provided by Marvel. © 2024 MARVEL</a>"
    ),
    highlightedPublication = Publication(
        id = "183",
        title = "Startling Stories: The Incorrigible Hulk (2004) #1",
        thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
        description = "For Doctor Bruce Banner life is anything but normal. But what happens when"
    ),
    publications = listOf(
        Publication(
            id = "183",
            title = "Startling Stories: The Incorrigible Hulk (2004) #1",
            thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
            description = "For Doctor Bruce Banner life is anything but normal. But what happens when")
    )
)

val marvelJsonString =
    "{\"code\":200,\"status\":\"Ok\",\"copyright\":\"© 2024 MARVEL\",\"attributionText\":\"Data provided by Marvel. © 2024 MARVEL\"," +
            "\"attributionHTML\":\"<a href=\\\"http://marvel.com\\\">Data provided by Marvel. © 2024 MARVEL</a>\",\"etag\":\"4ef8c626d3622fc9a6b1f0aea58027c532c8cc26\"," +
            "\"data\":{\"offset\":0,\"limit\":1,\"total\":48347,\"count\":1,\"results\":[{\"id\":183,\"digitalId\":0,\"title\":\"Startling Stories: The Incorrigible Hulk (2004) #1\"," +
            "\"issueNumber\":1,\"variantDescription\":\"\",\"description\":\"\",\"modified\":\"-0001-11-30T00:00:00-0500\",\"isbn\":\"\",\"upc\":\"5960605429-00811\",\"diamondCode\":\"\"," +
            "\"ean\":\"\",\"issn\":\"\",\"format\":\"Comic\",\"pageCount\":0,\"textObjects\":[{\"type\":\"issue_solicit_text\",\"language\":\"en-us\",\"text\":\"For Doctor Bruce Banner life " +
            "is anything but normal. But what happens when\"}]," +
            "\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/183\",\"urls\":[{\"type\":\"detail\",\"url\":" +
            "\"http://marvel.com/comics/issue/183/startling_stories_the_incorrigible_hulk_2004_1?utm_campaign=apiRef&utm_source=115bfdff31100c991b814e9cafa43527\"}]," +
            "\"series\":{\"resourceURI\":\"http://gateway.marvel.com/v1/public/series/565\",\"name\":\"Startling Stories: The Incorrigible Hulk (2004)\"},\"variants\":[],\"collections\":[]," +
            "\"collectedIssues\":[],\"dates\":[{\"type\":\"onsaleDate\",\"date\":\"2029-12-31T00:00:00-0500\"},{\"type\":\"focDate\",\"date\":\"-0001-11-30T00:00:00-0500\"}],\"prices\":[{\"type\":\"printPrice\"," +
            "\"price\":2.99}],\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\",\"extension\":\"jpg\"},\"images\":[],\"creators\":{\"available\":1,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/183/creators\"," +
            "\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/6291\",\"name\":\"Peter Bagge\",\"role\":\"penciller (cover)\"}],\"returned\":1},\"characters\":" +
            "{\"available\":1,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/183/characters\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/characters/1009351\"," +
            "\"name\":\"Hulk\"}],\"returned\":1},\"stories\":{\"available\":2,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/183/stories\"," +
            "\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/1891\",\"name\":\"Cover #1891\",\"type\":\"cover\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/1892\"," +
            "\"name\":\"Interior #1892\",\"type\":\"interiorStory\"}],\"returned\":2},\"events\":{\"available\":0,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/183/events\",\"items\":[],\"returned\":0}}]}}"