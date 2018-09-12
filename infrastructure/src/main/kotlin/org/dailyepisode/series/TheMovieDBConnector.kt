package org.dailyepisode.series

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException

interface TheMovieDBConnector {
  fun fetchSearchResultForPage(query: String, page: Int): TheMovieDBSeriesSearchResult
  fun fetchLookupResult(remoteId: Int): TheMovieDBLookupResult?
  fun fetchUpdatesForPage(page: Int): TheMovieDBUpdateResult
}

@Component
class TheMovieDBConnectorImpl(templateBuilder: RestTemplateBuilder,
                              @Value("\${themoviedb.base_url}") baseUrl: String,
                              private @Value("\${themoviedb.api_key}") val apiKey: String)
  : TheMovieDBConnector {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun fetchSearchResultForPage(query: String, page: Int): TheMovieDBSeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=$query&page=$page"
    return restTemplate.getForEntity(resource, TheMovieDBSeriesSearchResult::class.java).body!!
  }

  override fun fetchLookupResult(remoteId: Int): TheMovieDBLookupResult? {
    val resource = "/tv/$remoteId?api_key=$apiKey"
    return try {
      restTemplate.getForEntity(resource, TheMovieDBLookupResult::class.java).body!!
    } catch(ex: HttpClientErrorException) {
      if (isStatusCodeNotFound(ex)) null
      else throw ex
    }
  }

  private fun isStatusCodeNotFound(ex: HttpClientErrorException)=
    ex.statusCode.value() == 404

  override fun fetchUpdatesForPage(page: Int): TheMovieDBUpdateResult {
    val resource = "/tv/changes?api_key=$apiKey&page=$page"
    return restTemplate.getForEntity(resource, TheMovieDBUpdateResult::class.java).body!!
  }
}

interface TheMovieDBImageUrlResolver {
  fun resolveUrl(poster_path: String?): String?
}

@Component
internal class TheMovieDBImageUrlResolverImpl(private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                                              private @Value("\${themoviedb.thumbnail_size}") val thumbnailSize: String)
  : TheMovieDBImageUrlResolver {

  override fun resolveUrl(poster_path: String?): String? =
    if (poster_path != null) {
      "$imageBaseUrl/w$thumbnailSize$poster_path"
    } else null
}

class TheMovieDBSeriesSearchResult(
  val results: List<TheMovieDBSeriesSearchInfo>,
  val page: Int,
  val total_pages: Int,
  val total_result: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TheMovieDBSeriesSearchInfo(
  val id: Int,
  val name: String,
  val overview: String,
  val poster_path: String?,
  val vote_count: Int,
  val vote_average: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TheMovieDBLookupResult(
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
data class TheMovieDbGenre(val name: String)

data class TheMovieDBUpdateResult(
  val results: List<TheMovieDBChangedSeries>,
  val page: Int,
  val total_pages: Int,
  val total_result: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TheMovieDBChangedSeries(val id: Int)