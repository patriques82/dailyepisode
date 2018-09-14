package org.dailyepisode.series

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class SeriesBatchServiceTest {

  @Test(expected = RemoteIdNullPointerException::class)
  fun `lookup by remote ids with non-existent id should throw RemoteIdNullPointerException`() {
    val mockRemoteSeriesServiceFacade: RemoteSeriesServiceFacade = mockk(relaxed = true)
    every { mockRemoteSeriesServiceFacade.lookupByRemoteId(1) } returns null
    val seriesBatchService = SeriesBatchService(mockRemoteSeriesServiceFacade)

    val seriesLookupResults = seriesBatchService.lookupByRemoteIds(listOf(1))
    assertThat(seriesLookupResults).isEqualTo(emptyList<SeriesLookupResult>())
  }

  @Test
  fun `lookup by remote ids with only existing ids should throw RemoteIdNullPointerException`() {
    val mockRemoteSeriesServiceFacade: RemoteSeriesServiceFacade = mockk(relaxed = true)
    val friends = SeriesLookupResult(1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15)
    val homeland = SeriesLookupResult(2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 6, 68)
    every { mockRemoteSeriesServiceFacade.lookupByRemoteId(1) } returns friends
    every { mockRemoteSeriesServiceFacade.lookupByRemoteId(2) } returns homeland
    val seriesBatchService = SeriesBatchService(mockRemoteSeriesServiceFacade)

    val seriesLookupResults = seriesBatchService.lookupByRemoteIds(listOf(1, 2))
    assertThat(seriesLookupResults).isEqualTo(listOf(friends, homeland))
  }
}