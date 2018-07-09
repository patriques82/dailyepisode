package org.dailyepisode.series

import org.springframework.stereotype.Service
import java.util.*

fun SeriesEntity.toSeries(): Series {
  return SeriesShort(this.name, this.description, this.url, this.id)
}

@Service
internal class JpaDbService(val seriesReposiotory: SeriesRepository) {
  fun fetchByName(name: String): Optional<Series> = seriesReposiotory.findByName(name).map { it.toSeries() }
}
