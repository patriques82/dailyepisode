package org.dailyepisode.series

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

internal const val MAX_PAGES = 5

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
    } while (page < searchResult.total_pages && page < MAX_PAGES)
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

  override fun lookup(remoteId: Int): SeriesLookupInfo? {
    val resource = "/tv/$remoteId?api_key=$apiKey"
    val lookupResult = try {
      restTemplate.getForEntity(resource, TheMovieDBLookupResult::class.java).body!!
    } catch (ex: HttpClientErrorException) {
      null
    }
    return lookupResult?.toSeriesLookupInfo()
  }

  private fun TheMovieDBLookupResult.toSeriesLookupInfo() =
    SeriesLookupInfo(
      id, name, overview, createImageUrl(poster_path), vote_count, vote_average, first_air_date,
      last_air_date, genres.map { it.name }, homepage, number_of_episodes, number_of_seasons)

  override fun changesSinceYesterday(): SeriesChangedResult {
    val changedSeriesIds = fetchChangeResultAllPages()
    return SeriesChangedResult(changedSeriesIds)
  }

  private fun fetchChangeResultAllPages(): List<Int> {
    var page = 0
    val changedSeriesIds = mutableListOf<Int>()
    do {
      page++
      val changeResult = fetchChangedResultForPage(page)
      changedSeriesIds += changeResult.results.map { it.id }
    } while (page < changeResult.total_pages && page < MAX_PAGES)
    return changedSeriesIds
  }

  private fun fetchChangedResultForPage(page: Int): TheMovieDBChangesResult {
    val resource = "/tv/changes?api_key=$apiKey&page=$page"
    return restTemplate.getForEntity(resource, TheMovieDBChangesResult::class.java).body!!
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

internal data class TheMovieDBChangesResult(
  val results: List<TheMovieDBChangedSeries>,
  val page: Int,
  val total_pages: Int,
  val total_result: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class TheMovieDBChangedSeries(val id: Int)
