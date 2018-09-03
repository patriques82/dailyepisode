package org.dailyepisode.series

import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertThat
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class ImdbSearchServiceSpec: Spek({

  given("a imdb search service") {
    val baseUrl = "dummy-base-url"
    val apiKey = "dummy-api-key"
    val imageUrl = "dummy-image-url"
    val existingResultQuery = "friends"
    val nonExistingResultQuery = "non-friends"
    val imdbSeriesInfo = ImdbSeriesSearchInfo(1, "friends", "a couple of friends", "/friends.com/111", 11, 9.1)
    val successImdbSeriesSearchResult = ImdbSeriesSearchResult(listOf(imdbSeriesInfo))
    val failureImdbSeriesSearchResult = ImdbSeriesSearchResult(emptyList())

    val restTemplateMock = mockk<RestTemplate>()
    every {
      restTemplateMock.getForEntity("/search/tv?api_key=$apiKey&query=$existingResultQuery", ImdbSeriesSearchResult::class.java)
    } returns ResponseEntity.ok(successImdbSeriesSearchResult)
    every {
      restTemplateMock.getForEntity("/search/tv?api_key=$apiKey&query=$nonExistingResultQuery", ImdbSeriesSearchResult::class.java)
    } returns ResponseEntity.ok(failureImdbSeriesSearchResult)

    val restTemplateBuilderMock = mockk<RestTemplateBuilder>()
    every { restTemplateBuilderMock.rootUri(baseUrl) } returns restTemplateBuilderMock
    every { restTemplateBuilderMock.build() } returns restTemplateMock

    val imdbSearchService = ImdbSeriesSearchService(restTemplateBuilderMock, baseUrl, imageUrl, apiKey)

    on("calling a search query with one existing result") {
      val seriesSearchResult = imdbSearchService.search(SeriesSearchRequest(existingResultQuery))

      it("should return the existing result") {
        val expectedSeriesInfo = SeriesSearchInfo(1, "friends", "a couple of friends", "${imageUrl}/friends.com/111", 11, 9.1)
        val expectedSearchResult = SeriesSearchResult(listOf(expectedSeriesInfo))
        assertThat(seriesSearchResult, equalTo(expectedSearchResult))
      }
    }

    on("calling a search query with non-existing result") {
      val seriesSearchResult = imdbSearchService.search(SeriesSearchRequest(nonExistingResultQuery))

      it("should return a empty result") {
        val expectedSearchResult = SeriesSearchResult(emptyList())
        assertThat(seriesSearchResult, equalTo(expectedSearchResult))
      }
    }
  }
})
