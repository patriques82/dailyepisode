package org.dailyepisode.series

interface SeriesChangesService {
  fun changes(): SeriesChangedResult
}

data class SeriesChangedResult(val changedSeriesRemoteIds: List<Int>)