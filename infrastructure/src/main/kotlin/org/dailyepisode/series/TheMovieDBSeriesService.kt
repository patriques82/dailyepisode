package org.dailyepisode.series

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
internal class TheMovieDBSeriesService(templateBuilder: RestTemplateBuilder,
                                       @Value("\${themoviedb.base_url}") baseUrl: String,
                                       private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                                       private @Value("\${themoviedb.thumbnail_size}") val thumbnailSize: String,
                                       private @Value("\${themoviedb.api_key}") val apiKey: String) : SeriesService {
  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${seriesSearchRequest.query}"
    val searchResult = restTemplate.getForEntity(resource, TheMovieDBSeriesSearchResult::class.java)
      .body!!.results
    return SeriesSearchResult(searchResult.map { it.toSeriesSearchInfo() })
  }

  fun TheMovieDBSeriesSearchInfo.toSeriesSearchInfo() =
    SeriesSearchInfo(id, name, overview, createImageUrl(poster_path), vote_count, vote_average)

  override fun lookup(remoteId: Int): SeriesLookupInfo? {
    val resource = "/tv/$remoteId?api_key=$apiKey"
    val lookupResult = restTemplate.getForEntity(resource, TheMovieDBLookupResult::class.java).body!!
    return lookupResult.toSeriesLookupInfo()
  }

  fun TheMovieDBLookupResult.toSeriesLookupInfo() =
    SeriesLookupInfo(
      id, name, overview, createImageUrl(poster_path), vote_count, vote_average, first_air_date,
      last_air_date, genres.map { it.name }, homepage, number_of_episodes, number_of_seasons)

  override fun changes(): SeriesChangedResult {
    TODO("not implemented")
  }

  private fun createImageUrl(poster_path: String?): String =
    "$imageBaseUrl/w$thumbnailSize$poster_path"

}

internal class TheMovieDBSeriesSearchResult(val results: List<TheMovieDBSeriesSearchInfo>)

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
internal data class TheMovieDbGenre(
  val name: String
)
