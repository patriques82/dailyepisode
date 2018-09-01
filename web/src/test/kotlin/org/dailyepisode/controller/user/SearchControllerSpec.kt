package org.dailyepisode.controller.user

import io.mockk.every
import io.mockk.mockk
import org.dailyepisode.controller.user.SearchController
import org.dailyepisode.dto.SeriesInfoDto
import org.dailyepisode.dto.SeriesSearchResultDto
import org.dailyepisode.search.SearchService
import org.dailyepisode.search.SeriesInfo
import org.dailyepisode.search.SeriesSearchRequest
import org.dailyepisode.search.SeriesSearchResult
import org.hamcrest.CoreMatchers.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertThat
import org.springframework.http.ResponseEntity

class SearchControllerSpec : Spek({

  given("a search controller") {

    val successResult = SeriesSearchResult(listOf(SeriesInfo(1, "breaking bad", "overview", "imageurl", 9, 8.7)))
    val emptyResult = SeriesSearchResult(emptyList())
    val searchServiceMock = mockk<SearchService>()
    every { searchServiceMock.search(any()) } returns emptyResult
    every { searchServiceMock.search(SeriesSearchRequest("breaking")) } returns successResult

    val searchController = SearchController(searchServiceMock)

    on("calling a search query with one existing result") {
      val responseEntity: ResponseEntity<SeriesSearchResultDto> = searchController.search("breaking")
      val seriesSearchResultDto = responseEntity.body

      it("should return the existing result") {
        val expectedSeriesInfoDto = SeriesInfoDto(1, "breaking bad", "overview", "imageurl", 9, 8.7)
        val expectedSearchResultDto = SeriesSearchResultDto(listOf(expectedSeriesInfoDto))
        assertThat(seriesSearchResultDto, equalTo(expectedSearchResultDto))
      }
    }

    on("calling a search query with non-existing result") {
      val responseEntity: ResponseEntity<SeriesSearchResultDto> = searchController.search("never heard of this...")
      val seriesSearchResultDto = responseEntity.body

      it("should return a empty result") {
        val expectedSearchResultDto = SeriesSearchResultDto(emptyList())
        assertThat(seriesSearchResultDto, equalTo(expectedSearchResultDto))
      }
    }
  }
})