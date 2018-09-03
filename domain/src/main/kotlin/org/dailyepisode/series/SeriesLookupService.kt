package org.dailyepisode.series

interface SeriesLookupService {
  fun lookup(remoteId: Int): SeriesLookupInfo?
}

data class SeriesLookupInfo(
  val remoteId: Int,
  val name: String,
  val overview: String,
  val imageUrl: String,
  val voteCount: Int,
  val voteAverage: Double,
  val firstAirDate: String,
  val lastAirDate: String,
  val genres: List<String>,
  val homepage: String,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int
)