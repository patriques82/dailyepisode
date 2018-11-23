package org.dailyepisode.controller.user

import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.series.*
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SeriesControllerTest: AbstractControllerIntegrationTest() {

  @MockBean
  private lateinit var theMovieDBConnector: TheMovieDBConnector

  @Test
  fun `search should return series search result and 200 Ok`() {
    val gameOfThrones = TheMovieDBSeriesSearchInfo(1, "game of thrones", "Seven noble families fight", "thrones", 10, 9.2)
    val gameShakers = TheMovieDBSeriesSearchInfo(1, "game shakers", "12-year-old girls are incredible", "shakers", 22, 9.3)
    val jokerGame = TheMovieDBSeriesSearchInfo(1, "joker game", "mysterious spy training organization", "joker", 5, 8.5)

    val searchResultPage1 = TheMovieDBSeriesSearchResult(listOf(gameOfThrones, gameShakers), 1, 2, 3)
    given(theMovieDBConnector.fetchSearchResultForPage("game", 1)).willReturn(searchResultPage1)
    val searchResultPage2 = TheMovieDBSeriesSearchResult(listOf(jokerGame), 2, 2, 3)
    given(theMovieDBConnector.fetchSearchResultForPage("game", 2)).willReturn(searchResultPage2)

    val expectedJson = """
      {"results": [
        {"remoteId":1,
         "name":"game of thrones",
         "overview":"Seven noble families fight",
         "imageUrl":"http://image.tmdb.org/t/p/w92/thrones",
         "voteCount":10,"voteAverage":9.2},
        {"remoteId":1,
         "name":"game shakers",
         "overview":"12-year-old girls are incredible",
         "imageUrl":"http://image.tmdb.org/t/p/w92/shakers",
         "voteCount":22,
         "voteAverage":9.3}
      ],
      "page": 1,
      "totalPages": 2,
      "totalResult": 3}
    """.trimIndent()

    mockMvc.perform(get("/api/series/search/1?query=game")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson, true))

  }

  @Test
  fun `lookup with existing remote id should return series and 200 Ok`() {
    val nextEpisodeToAir = NextEpisodeToAir("2018-03-23", 1)
    val lastEpisodeToAir = LastEpisodeToAir("2018-03-03", 9)
    val lookupResult = TheMovieDBLookupResult(1, "game of thrones", "Seven noble families fight", "thrones", 10, 9.2,
      "2011-01-01", "2018-03-03", listOf(TheMovieDbGenre("drama"), TheMovieDbGenre("fantasy")), nextEpisodeToAir,lastEpisodeToAir, "www.got.com", 77, 8)
    given(theMovieDBConnector.fetchLookupResult(1)).willReturn(lookupResult)

    val expectedJson = """
      {"remoteId":1,
       "name":"game of thrones",
       "overview":"Seven noble families fight",
       "imageUrl":"http://image.tmdb.org/t/p/w92/thrones",
       "voteCount":10,
       "voteAverage":9.2,
       "firstAirDate":"2011-01-01",
       "lastAirDate":"2018-03-03",
       "genres":["drama","fantasy"],
       "homepage":"www.got.com",
       "numberOfEpisodes":77,
       "numberOfSeasons":8}
    """.trimIndent()

    mockMvc.perform(get("/api/series/1")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().json(expectedJson, true))
  }

  @Test
  fun `lookup with non-existing remote id should return 204 No Content`() {
    given(theMovieDBConnector.fetchLookupResult(1)).willReturn(null)

    mockMvc.perform(get("/api/series/1")
      .with(csrf())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent)
      .andReturn()
  }

}