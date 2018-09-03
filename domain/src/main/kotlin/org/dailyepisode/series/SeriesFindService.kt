package org.dailyepisode.series

interface SeriesFindService {
  fun find(remoteId: Int): SeriesInfo?
}

class SeriesInfo