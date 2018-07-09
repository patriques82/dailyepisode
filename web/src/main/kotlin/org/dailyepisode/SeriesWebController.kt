package org.dailyepisode

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/web")
class SeriesWebController(val seriesWebService: SeriesWebService) {

  @GetMapping("/")
  fun query(@RequestParam("query") query: String?) = seriesWebService.fetchForQuery(query ?: "Breaking")

}