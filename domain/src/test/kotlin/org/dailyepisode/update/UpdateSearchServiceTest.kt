package org.dailyepisode.update

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesUpdatedLookupResult
import org.dailyepisode.series.UpdatedSeriesResult
import org.dailyepisode.subscription.Subscription
import org.junit.Test

class UpdateSearchServiceTest {

  @Test
  fun `search for updates with non-updated subscriptions should return empty list`() {
    val mockRemoteSeriesServiceFacade: RemoteSeriesServiceFacade = mockk()
    every { mockRemoteSeriesServiceFacade.updatesSinceYesterday() } returns UpdatedSeriesResult(listOf(3))
    val updateSearchService = UpdateSearchService(mockRemoteSeriesServiceFacade)

    val friends = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15)
    val homeland = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 6, 68)
    val subscriptions = listOf(friends, homeland)

    val updateLookupResults = updateSearchService.searchForUpdates(subscriptions)

    assertThat(updateLookupResults).isEmpty()
  }

  @Test
  fun `search for updates with updated subscriptions should return list of updated lookup results`() {
    val mockRemoteSeriesServiceFacade: RemoteSeriesServiceFacade = mockk()
    every { mockRemoteSeriesServiceFacade.updatesSinceYesterday() } returns UpdatedSeriesResult(listOf(2))
    val updatedHomeland = SeriesLookupResult(2, "homeland", "terrorist chasing carrie", "newimage", 100, 10.0, "2008-01-23", "2018-05-19", listOf(), "www.homeland.com", 77, 6)
    every { mockRemoteSeriesServiceFacade.lookup(2) } returns updatedHomeland
    val updateSearchService = UpdateSearchService(mockRemoteSeriesServiceFacade)

    val friends = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15)
    val homeland = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 68, 5)
    val subscriptions = listOf(friends, homeland)
    val updateLookupResults = updateSearchService.searchForUpdates(subscriptions)

    val expectedUpdatedLookupResult = SeriesUpdatedLookupResult(2, "newimage", "2018-05-19", 77, 6)
    assertThat(updateLookupResults).isEqualTo(listOf(expectedUpdatedLookupResult))
  }
}