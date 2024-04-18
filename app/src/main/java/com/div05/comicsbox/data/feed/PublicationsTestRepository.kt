package com.div05.comicsbox.data.feed

import com.div05.comicsbox.data.Operation
import com.div05.comicsbox.model.Attribution
import com.div05.comicsbox.model.Publication
import com.div05.comicsbox.model.PublicationsFeed
import kotlinx.coroutines.flow.Flow

class PublicationsTestRepository: PublicationsRepository {
    override suspend fun getPublication(postId: String?): Operation<Publication> {
        TODO("Not yet implemented")
    }

    override suspend fun getPublicationsFeed(): Operation<PublicationsFeed> {
        TODO("Not yet implemented")
    }

    override suspend fun observePublicationsFeed(): Flow<PublicationsFeed?> {
        TODO("Not yet implemented")
    }
}

//data class Publication( val id : Int, val titleImageUrl: String, val title: String, val description: String)
//data class SampleData( val publications: List<Publication>) {
//    companion object {
//        val publicationsSample: List<Publication> = listOf(
//            Publication(1994,
//                "http://i.annihil.us/u/prod/marvel/i/mg/f/20/4bc63a47b8dcb.jpg",
//                "Official Handbook of the Marvel Universe (2004) #13 (TEAMS)",
//                "Heavy-hitting heroes unite! This Official Handbook contains in-depth bios on more than 30 of the Marvel Universe's most awesome assemblages - including the Defenders, Power Pack and the New Thunderbolts! Plus: An all-new cover by superstar artist Tom Grummett, digitally painted by Morry Hollowell.\r<br>48 PGS./All Ages ...$3.99\r<br>"),
//            Publication(5813,
//                "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
//                "Marvel Milestones (2005) #22",
//                ""),
//            Publication(384,
//                "http://i.annihil.us/u/prod/marvel/i/mg/c/60/4bc69f11baf75.jpg",
//                "Gun Theory (2003) #3",
//                "The phone rings, and killer-for-hire Harvey embarks on another hit. But nothing's going right this job. There's little room for error in the business of killing - so what happens when one occurs?\\r\\n\\r\\n32 PGS./ PARENTAL ADVISORY ...\$2.50"),
//            Publication(1886,
//                "http://i.annihil.us/u/prod/marvel/i/mg/b/40/4bc64020a4ccc.jpg",
//                "Official Handbook of the Marvel Universe (2004) #12 (SPIDER-MAN)",
//                "The spectacular sequel to last year's OFFICIAL HANDBOOK OF THE MARVEL UNIVERSE: SPIDER-MAN 2004, this Official Handbook contains in-depth bios on more than 30 of the wisecracking web-slinger's closest allies and most infamous enemies - including the Stacy Twins, fresh from the pages of AMAZING SPIDER-MAN, and Toxin, in time for this month's TOXIN #1! Plus: An all-new cover by superstar artist Tom Raney, digitally painted by Morry Hollowell.\\r<br>48 PGS./Marvel PSR ...\$3.99\\r<br>"),
//            Publication(428,
//                "http://i.annihil.us/u/prod/marvel/i/mg/4/20/4bc697c680890.jpg",
//                "Ant-Man (2003) #4",
//                "Ant-Man digs deeper into finding out who is leaking those dirty little secrets that are threatening our national security. And who's better at uncovering dirty LITTLE secrets than him??")
//        )
//    }
//}