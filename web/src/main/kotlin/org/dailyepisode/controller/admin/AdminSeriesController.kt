package org.dailyepisode.controller.admin

import org.dailyepisode.series.UpdatedSeriesResult
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/series")
@CrossOrigin(origins = arrayOf("http://localhost:3000"))
class AdminSeriesController(val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  @GetMapping("/changes")
  fun changes(): ResponseEntity<UpdatedSeriesResult> {
    val changes = remoteSeriesServiceFacade.updatesSinceYesterday()
    return ResponseEntity(changes, HttpStatus.OK)
  }

}