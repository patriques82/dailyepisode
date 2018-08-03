package org.dailyepisode.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.boot.web.client.RestTemplateBuilder

@Service
class ImdbSearchService(templateBuilder: RestTemplateBuilder,
                        @Value("\${themoviedb.base_url}") val baseUrl: String,
                        @Value("\${themoviedb.api_key}") val apiKey: String): SearchService {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${seriesSearchRequest.query}"
    val result = restTemplate.getForEntity(resource, ImdbSeriesSearchResult::class.java)
    val imdbSeriesSearchResult = result.body
    return SeriesSearchResult(imdbSeriesSearchResult.results.map {
     SeriesInfo(it.id, it.name, it.overview)
    })
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ImdbSeriesSearchResult(val results: List<ImdbSeriesInfo>)

@JsonIgnoreProperties(ignoreUnknown = true)
class ImdbSeriesInfo(val id: Int, val name: String, val overview: String)
