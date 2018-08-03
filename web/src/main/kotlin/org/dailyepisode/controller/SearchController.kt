package org.dailyepisode.controller

import org.dailyepisode.search.SearchService
import org.dailyepisode.search.SeriesSearchRequest
import org.dailyepisode.search.SeriesSearchResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(val searchService: SearchService) {

  @GetMapping
  fun search(@RequestParam("query") query: String?): ResponseEntity<SeriesSearchResult> {
    val searchResult = query?.let {
      searchService.search(SeriesSearchRequest(it))
    } ?: SeriesSearchResult(listOf())
    return ResponseEntity.ok(searchResult)
  }
}
