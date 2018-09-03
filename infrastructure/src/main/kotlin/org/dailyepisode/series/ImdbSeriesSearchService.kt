package org.dailyepisode.series

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
internal class ImdbSeriesSearchService(templateBuilder: RestTemplateBuilder,
                                       @Value("\${themoviedb.base_url}") baseUrl: String,
                                       private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                                       private @Value("\${themoviedb.api_key}") val apiKey: String) : SeriesSearchService {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${seriesSearchRequest.query}"
    val searchResult = restTemplate.getForEntity(resource, ImdbSeriesSearchResult::class.java)
      .body!!.results
    return SeriesSearchResult(searchResult.map { it.toSeriesInfo() })
  }

  private fun ImdbSeriesSearchInfo.toSeriesInfo() =
    SeriesSearchInfo(id, name, overview, "$imageBaseUrl${poster_path}", vote_count, vote_average)

}

internal class ImdbSeriesSearchResult(val results: List<ImdbSeriesSearchInfo>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImdbSeriesSearchInfo(
  val id: Int,
  val name: String,
  val overview: String,
  val poster_path: String?,
  val vote_count: Int,
  val vote_average: Double
)
