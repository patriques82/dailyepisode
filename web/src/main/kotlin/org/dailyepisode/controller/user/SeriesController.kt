package org.dailyepisode.controller.user

import org.dailyepisode.dto.SeriesLookupInfoDto
import org.dailyepisode.dto.SeriesSearchResultDto
import org.dailyepisode.dto.toDto
import org.dailyepisode.series.SeriesSearchRequest
import org.dailyepisode.series.SeriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/series")
class SeriesController(private val seriesService: SeriesService) {

  @GetMapping("/search")
  fun search(@RequestParam("query") query: String?): ResponseEntity<SeriesSearchResultDto> {
    val searchResult = query?.let {
      val seriesResult = seriesService.search(SeriesSearchRequest(it))
      SeriesSearchResultDto(seriesResult.results.map { it.toDto() })
    } ?: SeriesSearchResultDto(listOf())
    return ResponseEntity(searchResult, HttpStatus.OK)
  }

  @GetMapping("/{remoteId}")
  fun lookup(@PathVariable remoteId: Int): ResponseEntity<SeriesLookupInfoDto> {
    val lookupResult = seriesService.lookup(remoteId)
    if (lookupResult == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
    return ResponseEntity(lookupResult.toDto(), HttpStatus.OK)
  }

}