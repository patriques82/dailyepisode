package org.dailyepisode.hello

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

interface SeriesWebService {
  fun fetchForQuery(query: String): String
}

@Service
internal class TheMovieDBWebService(templateBuilder: RestTemplateBuilder,
                                    @Value("\${themoviedb.base_url}") val baseUrl: String,
                                    @Value("\${themoviedb.api_key}") val apiKey: String) : SeriesWebService {

  val restTemplate = templateBuilder.rootUri(baseUrl).build()

  override fun fetchForQuery(query: String): String {
    val resource = "/search/tv?api_key=$apiKey&query=$query"
    val result = restTemplate.getForEntity(resource, String::class.java)
    return result.body ?: "Not found"
  }


}

