package org.dailyepisode.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
internal class ImdbSeriesSearchResult(val results: List<ImdbSeriesInfo>)