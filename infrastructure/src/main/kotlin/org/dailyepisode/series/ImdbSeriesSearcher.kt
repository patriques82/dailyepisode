package org.dailyepisode.series

import org.springframework.web.client.RestTemplate

class ImdbSeriesSearcher(val restTemplate: RestTemplate, val apiKey: String): SeriesSearcher<SearchRequest, SearchResult> {

  override fun search(searchRequest: SearchRequest): SearchResult {
    val resource = "/search/tv?api_key=$apiKey&query=${searchRequest.query}"
    val result = restTemplate.getForEntity(resource, String::class.java)
    val res = result.body ?: "Not found"
    return ImdbSearchResult(res)
  }

}

class ImdbSearchRequest(override val query: String) : SearchRequest


class ImdbSearchResult(override val result: String) : SearchResult
