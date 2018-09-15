package org.dailyepisode.controller.user

import org.dailyepisode.dto.SeriesLookupResultDto
import org.dailyepisode.dto.SeriesSearchResultDto
import org.dailyepisode.dto.toDto
import org.dailyepisode.series.SeriesSearchRequest
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/series")
class SeriesController(private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  @GetMapping("/search")
  fun search(@RequestParam("query") query: String?): ResponseEntity<SeriesSearchResultDto> {
    val searchResult =
      if (query != null) {
        val seriesResult = remoteSeriesServiceFacade.search(SeriesSearchRequest(query))
        SeriesSearchResultDto(seriesResult.results.map { it.toDto() })
      } else {
        SeriesSearchResultDto(listOf())
      }
    return ResponseEntity(searchResult, HttpStatus.OK)
  }

  @GetMapping("/{remoteId}")
  fun lookup(@PathVariable remoteId: Int): ResponseEntity<SeriesLookupResultDto> {
    val lookupResult = remoteSeriesServiceFacade.lookup(remoteId)
    if (lookupResult == null) {
      return ResponseEntity(HttpStatus.NO_CONTENT)
    }
    return ResponseEntity(lookupResult.toDto(), HttpStatus.OK)
  }

}
