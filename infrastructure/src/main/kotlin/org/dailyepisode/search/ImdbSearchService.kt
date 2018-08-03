package org.dailyepisode.search

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class ImdbSearchService(templateBuilder: RestTemplateBuilder,
                        @Value("\${themoviedb.base_url}") baseUrl: String,
                        @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                        private @Value("\${themoviedb.api_key}") val apiKey: String) : SearchService {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${seriesSearchRequest.query}"
    val searchResult = restTemplate.getForEntity(resource, ImdbSeriesSearchResult::class.java)
      .body!!.results
    return SeriesSearchResult(searchResult.map { it.toSeriesInfo(imageBaseUrl) })
  }

}

private fun ImdbSeriesInfo.toSeriesInfo(imageBaseUrl: String): SeriesInfo {
  val thumbnailUrl = "${imageBaseUrl}/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
  return SeriesInfo(id, name, overview, thumbnailUrl, vote_count, vote_average)
}

