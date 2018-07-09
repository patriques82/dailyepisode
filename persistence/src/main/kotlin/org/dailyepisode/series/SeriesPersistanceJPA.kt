package org.dailyepisode.series

import org.springframework.stereotype.Service
import java.util.*

@Service
class SeriesPersistanceJPA: SeriesPersistance {
  override fun getSeriesById(id: Long): Optional<Series> {
    return Optional.of(SeriesShort("cain", "cain", "cairn", 3))
  }

  override fun save(series: Series) {
  }

}
