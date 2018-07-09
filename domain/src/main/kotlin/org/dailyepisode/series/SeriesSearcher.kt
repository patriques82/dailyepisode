package org.dailyepisode.series

interface SeriesSearcher<in REQ: SearchRequest, out RES: SearchResult> {
  fun search(searchRequest: REQ): RES
}

interface SearchRequest

interface SearchResult


