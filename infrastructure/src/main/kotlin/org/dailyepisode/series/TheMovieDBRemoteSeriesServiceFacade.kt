package org.dailyepisode.series

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

internal const val MAX_SEARCH_PAGES = 5
internal const val MAX_UPDATES_PAGES = 10

@Service
internal class TheMovieDBRemoteSeriesServiceFacade(private val theMovieDBConnector: TheMovieDBConnector,
                                                   private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                                                   private @Value("\${themoviedb.thumbnail_size}") val thumbnailSize: String)
  : RemoteSeriesServiceFacade {

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val searchResult = fetchSearchResultAllPages(seriesSearchRequest.query)
    return SeriesSearchResult(searchResult.map { it.toSeriesSearchInfo() })
  }

  private fun fetchSearchResultAllPages(query: String): List<TheMovieDBSeriesSearchInfo> {
    var page = 0
    val searchInfos = mutableListOf<TheMovieDBSeriesSearchInfo>()
    do {
      page++
      val searchResult = theMovieDBConnector.fetchSearchResultForPage(query, page)
      searchInfos += searchResult.results
    } while (page < searchResult.total_pages && page < MAX_SEARCH_PAGES)
    return searchInfos
  }

  private fun TheMovieDBSeriesSearchInfo.toSeriesSearchInfo() =
    SeriesSearchInfo(id, name, overview, resolveImageUrl(poster_path), vote_count, vote_average)

  private fun resolveImageUrl(poster_path: String?): String? =
    if (poster_path != null) "$imageBaseUrl/w$thumbnailSize/$poster_path" else null

  override fun lookup(remoteId: Int): SeriesLookupResult? {
    val lookupResult = theMovieDBConnector.fetchLookupResult(remoteId)
    return lookupResult?.toSeriesLookupResult()
  }

  private fun TheMovieDBLookupResult.toSeriesLookupResult() =
    SeriesLookupResult(
      id, name, overview, resolveImageUrl(poster_path), vote_count, vote_average, first_air_date,
      last_air_date, genres.map { it.name }, homepage, number_of_episodes, number_of_seasons)

  override fun updatesSinceYesterday(): SeriesUpdateResult {
    val updatedSeriesIds = fetchUpdatesAllPages()
    return SeriesUpdateResult(updatedSeriesIds)
  }

  private fun fetchUpdatesAllPages(): List<Int> {
    var page = 0
    val updatedSeriesIds = mutableListOf<Int>()
    do {
      page++
      val updates = theMovieDBConnector.fetchUpdatesForPage(page)
      updatedSeriesIds += updates.results.map { it.id }
    } while (page < updates.total_pages && page < MAX_UPDATES_PAGES)
    return updatedSeriesIds
  }

}