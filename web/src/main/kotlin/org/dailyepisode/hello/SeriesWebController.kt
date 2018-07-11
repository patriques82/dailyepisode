package org.dailyepisode.hello

import org.dailyepisode.series.ImdbSearchRequest
import org.dailyepisode.series.SearchRequest
import org.dailyepisode.series.SearchResult
import org.dailyepisode.series.SeriesSearcher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/web")
class SeriesWebController(val seriesSearcher: SeriesSearcher<SearchRequest, SearchResult>) {

  @GetMapping("/")
  fun query(@RequestParam("query") query: String?): SearchResult {
    val searchRequest = ImdbSearchRequest(query ?: "Breaking")
    return seriesSearcher.search(searchRequest)
  }

}