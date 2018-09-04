package org.dailyepisode.controller.admin

import org.dailyepisode.series.SeriesChangedResult
import org.dailyepisode.series.SeriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/series")
class AdminSeriesController(val seriesService: SeriesService) {

  @GetMapping("/changes")
  fun changes(): ResponseEntity<SeriesChangedResult> {
    val changes = seriesService.changesSinceYesterday()
    return ResponseEntity(changes, HttpStatus.OK)
  }
}