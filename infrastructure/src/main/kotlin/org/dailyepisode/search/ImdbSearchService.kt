package org.dailyepisode.search

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

class SeriesSearchRequest(val query: String)
class SeriesSearchResult(val results: List<ImdbSeriesInfoDto>)

@Service
class ImdbSearchService(templateBuilder: RestTemplateBuilder,
                        @Value("\${themoviedb.base_url}") baseUrl: String,
                        @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                        private @Value("\${themoviedb.api_key}") val apiKey: String) : SearchService {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${seriesSearchRequest.query}"
    val searchResult = restTemplate.getForEntity(resource, SeriesSearchResult::class.java)
      .body!!.results
    return SeriesSearchResult(appendBaseUrlToPosterPath(searchResult))
  }

  private fun appendBaseUrlToPosterPath(searchResult: List<ImdbSeriesInfoDto>) =
    searchResult.map { it.copy(poster_path = "$imageBaseUrl${it.poster_path}") }

}
