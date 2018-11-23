package org.dailyepisode.series

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

internal const val MAX_UPDATES_PAGES = 10

@Service
internal class TheMovieDBRemoteSeriesServiceFacade(private val theMovieDBConnector: TheMovieDBConnector,
                                                   private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                                                   private @Value("\${themoviedb.thumbnail_size}") val thumbnailSize: String)
  : RemoteSeriesServiceFacade {

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val searchResult = fetchSearchResult(seriesSearchRequest.query, seriesSearchRequest.page)
    return with(searchResult) {
      SeriesSearchResult(results.map { it.toSeriesSearchInfo() }, page, total_pages, total_result)
    }
  }

  private fun fetchSearchResult(query: String, page: Int): TheMovieDBSeriesSearchResult =
    theMovieDBConnector.fetchSearchResultForPage(query, page)

  private fun TheMovieDBSeriesSearchInfo.toSeriesSearchInfo() =
    SeriesSearchInfo(id, name, overview, resolveImageUrl(poster_path), vote_count ?: 0, vote_average ?: 0.0)

  private fun resolveImageUrl(poster_path: String?): String? =
    if (poster_path != null) "$imageBaseUrl/w$thumbnailSize/$poster_path" else null

  override fun lookup(remoteId: Int): SeriesLookupResult? {
    val lookupResult = theMovieDBConnector.fetchLookupResult(remoteId)
    return lookupResult?.toSeriesLookupResult()
  }

  private fun TheMovieDBLookupResult.toSeriesLookupResult() =
    SeriesLookupResult(
      id, name, overview, resolveImageUrl(poster_path), vote_count ?: 0, vote_average ?: 0.0, first_air_date,
      last_air_date, last_episode_to_air?.episode_number == 1, genres.map { it.name }, homepage, number_of_episodes ?: 0, number_of_seasons ?: 0,
      next_episode_to_air?.air_date, next_episode_to_air?.episode_number == 1)

}