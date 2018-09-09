package org.dailyepisode.series

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

internal const val MAX_SEARCH_PAGES = 5
internal const val MAX_UPDATES_PAGES = 10

@Service
internal class TheMovieDBSeriesService(templateBuilder: RestTemplateBuilder,
                                       @Value("\${themoviedb.base_url}") baseUrl: String,
                                       private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                                       private @Value("\${themoviedb.thumbnail_size}") val thumbnailSize: String,
                                       private @Value("\${themoviedb.api_key}") val apiKey: String) : SeriesService {
  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val searchResult = fetchSearchResultAllPages(seriesSearchRequest.query)
    return SeriesSearchResult(searchResult.map { it.toSeriesSearchInfo() })
  }

  private fun fetchSearchResultAllPages(query: String): List<TheMovieDBSeriesSearchInfo> {
    var page = 0
    val searchInfos = mutableListOf<TheMovieDBSeriesSearchInfo>()
    do {
      page++
      val searchResult = fetchSearchResultForPage(query, page)
      searchInfos += searchResult.results
    } while (page < searchResult.total_pages && page < MAX_SEARCH_PAGES)
    return searchInfos
  }

  private fun fetchSearchResultForPage(query: String, page: Int): TheMovieDBSeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=$query&page=$page"
    return restTemplate.getForEntity(resource, TheMovieDBSeriesSearchResult::class.java).body!!
  }

  private fun TheMovieDBSeriesSearchInfo.toSeriesSearchInfo() =
    SeriesSearchInfo(id, name, overview, createImageUrl(poster_path), vote_count, vote_average)

  private fun createImageUrl(poster_path: String?): String =
    "$imageBaseUrl/w$thumbnailSize$poster_path"

  override fun lookup(remoteId: Int): SeriesLookupResult? {
    val resource = "/tv/$remoteId?api_key=$apiKey"
    val lookupResult = try {
      restTemplate.getForEntity(resource, TheMovieDBLookupResult::class.java).body!!
    } catch (ex: HttpClientErrorException) {
      null
    }
    return lookupResult?.toSeriesLookupResult()
  }

  private fun TheMovieDBLookupResult.toSeriesLookupResult() =
    SeriesLookupResult(
      id, name, overview, createImageUrl(poster_path), vote_count, vote_average, first_air_date,
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
      val updates = fetchUpdatesForPage(page)
      updatedSeriesIds += updates.results.map { it.id }
    } while (page < updates.total_pages && page < MAX_UPDATES_PAGES)
    return updatedSeriesIds
  }

  private fun fetchUpdatesForPage(page: Int): TheMovieDBUpdateResult {
    val resource = "/tv/changes?api_key=$apiKey&page=$page"
    return restTemplate.getForEntity(resource, TheMovieDBUpdateResult::class.java).body!!
  }
}

internal class TheMovieDBSeriesSearchResult(
  val results: List<TheMovieDBSeriesSearchInfo>,
  val page: Int,
  val total_pages: Int,
  val total_result: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class TheMovieDBSeriesSearchInfo(
  val id: Int,
  val name: String,
  val overview: String,
  val poster_path: String?,
  val vote_count: Int,
  val vote_average: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class TheMovieDBLookupResult(
  val id: Int,
  val name: String,
  val overview: String,
  val poster_path: String?,
  val vote_count: Int,
  val vote_average: Double,
  val first_air_date: String,
  val last_air_date: String,
  val genres: List<TheMovieDbGenre>,
  val homepage: String,
  val number_of_episodes: Int,
  val number_of_seasons: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class TheMovieDbGenre(val name: String)

internal data class TheMovieDBUpdateResult(
  val results: List<TheMovieDBChangedSeries>,
  val page: Int,
  val total_pages: Int,
  val total_result: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class TheMovieDBChangedSeries(val id: Int)
