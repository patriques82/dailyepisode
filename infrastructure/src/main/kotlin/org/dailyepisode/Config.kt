package org.dailyepisode

import org.dailyepisode.series.ImdbSeriesSearcher
import org.dailyepisode.series.SearchRequest
import org.dailyepisode.series.SearchResult
import org.dailyepisode.series.SeriesSearcher
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

  @Bean
  fun seriesSearcher(templateBuilder: RestTemplateBuilder,
                     @Value("\${themoviedb.base_url}") baseUrl: String,
                     @Value("\${themoviedb.api_key}") apiKey: String): SeriesSearcher<SearchRequest, SearchResult> {
    val restTemplate = templateBuilder.rootUri(baseUrl).build()
    return ImdbSeriesSearcher(restTemplate, apiKey)
  }

}