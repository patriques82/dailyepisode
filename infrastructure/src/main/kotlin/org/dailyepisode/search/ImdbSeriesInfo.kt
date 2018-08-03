package org.dailyepisode.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ImdbSeriesInfo(val id: Int, val name: String, val overview: String)