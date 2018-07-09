package org.dailyepisode.series

import java.util.*

interface SeriesPersistance {
  fun getSeriesById(id: Long): Optional<Series>
  fun save(series: Series)
}
