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

  @GetMapping("/search/{page}")
  fun search(@PathVariable page: Int, @RequestParam("query") query: String?): ResponseEntity<SeriesSearchResultDto> {
    if (query != null && page > 0) {
      val seriesResult = remoteSeriesServiceFacade.search(SeriesSearchRequest(query, page))
      return ResponseEntity(seriesResult.toDto(), HttpStatus.OK)
    } else {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
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
