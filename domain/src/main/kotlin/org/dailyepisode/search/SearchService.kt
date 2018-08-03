package org.dailyepisode.search

interface SearchService {
  fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult
}

class SeriesSearchRequest(val query: String)

class SeriesSearchResult(val results: List<SeriesInfo>)
