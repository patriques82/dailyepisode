package org.dailyepisode.controller

import org.dailyepisode.dto.SeriesInfoDto
import org.dailyepisode.dto.SeriesSearchResultDto
import org.dailyepisode.search.SearchService
import org.dailyepisode.search.SeriesInfo
import org.dailyepisode.search.SeriesSearchRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(val searchService: SearchService) {

  @GetMapping
  fun search(@RequestParam("query") query: String?): ResponseEntity<SeriesSearchResultDto> {
    val searchResult = query?.let {
      val seriesResult = searchService.search(SeriesSearchRequest(it))
      SeriesSearchResultDto(results = seriesResult.results.map { it.toDto() })
    } ?: SeriesSearchResultDto(results = listOf())
    return ResponseEntity.ok(searchResult)
  }
}

fun SeriesInfo.toDto(): SeriesInfoDto =
  SeriesInfoDto(remoteId, name, overview, imageUrl, voteCount, voteAverage)
