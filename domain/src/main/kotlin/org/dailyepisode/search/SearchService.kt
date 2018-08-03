package org.dailyepisode.search

interface SearchService {
  fun search(seriesSearchRequest: SeriesSearchRequest): SeriesSearchResult
}

