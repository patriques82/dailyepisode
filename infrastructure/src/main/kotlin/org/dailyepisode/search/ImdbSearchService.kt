package org.dailyepisode.search

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class ImdbSearchService(templateBuilder: RestTemplateBuilder,
                        @Value("\${themoviedb.base_url}") val baseUrl: String,
                        @Value("\${themoviedb.api_key}") val apiKey: String) : SearchService {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${seriesSearchRequest.query}"
    val searchResult = restTemplate.getForEntity(resource, ImdbSeriesSearchResult::class.java)
      .body!!.results
    return SeriesSearchResult(searchResult.map { it.toSeriesInfo() })
  }
}

private fun ImdbSeriesInfo.toSeriesInfo(): SeriesInfo {
  return SeriesInfo(id, name, overview)
}

