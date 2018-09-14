package org.dailyepisode.controller.admin

import org.dailyepisode.series.SeriesUpdateResult
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/series")
class AdminSeriesController(val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  @GetMapping("/changes")
  fun changes(): ResponseEntity<SeriesUpdateResult> {
    val changes = remoteSeriesServiceFacade.updatesSinceYesterday()
    return ResponseEntity(changes, HttpStatus.OK)
  }

}