package org.dailyepisode.controller.user

import org.dailyepisode.controller.AbstractControllerIntegrationTest
import org.dailyepisode.series.TheMovieDBConnector
import org.dailyepisode.series.TheMovieDBImageUrlResolver
import org.springframework.boot.test.mock.mockito.MockBean

class SeriesControllerTest: AbstractControllerIntegrationTest() {

  @MockBean
  private lateinit var theMovieDBConnector: TheMovieDBConnector
  @MockBean
  private lateinit var theMovieDBImageUrlResolver: TheMovieDBImageUrlResolver



}