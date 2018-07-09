package org.dailyepisode

import org.dailyepisode.series.SeriesPersistance
import org.dailyepisode.series.SeriesRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired

@RestController
class DbController(@Autowired val seriesRepository: SeriesRepository,
                   @Autowired val seriesPersistance: SeriesPersistance) {

  @GetMapping("/{name}")
  fun fetchByName(@PathVariable name: String) = seriesPersistance.getSeriesById(3)
    //seriesRepository.findById(1)

}