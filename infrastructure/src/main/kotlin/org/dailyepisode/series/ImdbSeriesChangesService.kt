package org.dailyepisode.series

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder

class ImdbSeriesChangesService(templateBuilder: RestTemplateBuilder,
                               @Value("\${themoviedb.base_url}") baseUrl: String,
                               private @Value("\${themoviedb.image_base_url}") val imageBaseUrl: String,
                               private @Value("\${themoviedb.api_key}") val apiKey: String) : SeriesChangesService {

  private val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun changes(): SeriesChangedResult {
    TODO("not implemented")
  }
}