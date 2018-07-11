package org.dailyepisode.series

interface SeriesSearcher<in REQ: SearchRequest, out RES: SearchResult> {
  fun search(searchRequest: REQ): RES
}

interface SearchRequest {
  val query: String
}

interface SearchResult {
  val result: String
}


