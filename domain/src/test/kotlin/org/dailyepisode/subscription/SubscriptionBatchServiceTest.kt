package org.dailyepisode.subscription

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SubscriptionBatchServiceTest {

    @Test
    fun `find by remote ids with non-existent id should return with empty subscription list`() {
      val mockSubscriptionStorageService: SubscriptionStorageService = mockk()
      every { mockSubscriptionStorageService.findByRemoteId(1) } returns null
      val subscriptionBatchService = SubscriptionBatchService(mockSubscriptionStorageService)

      val expectedSubscriptionResult = listOf(1) to emptyList<Subscription>()

      val actualSubscriptionResult = subscriptionBatchService.findByRemoteIds(listOf(1))
      assertThat(actualSubscriptionResult).isEqualTo(expectedSubscriptionResult)
    }

    @Test
    fun `lookup by remote ids with only existing ids should throw RemoteIdNullPointerException`() {
      val mockSubscriptionStorageService: SubscriptionStorageService = mockk()
      val friends = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15)
      val homeland = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 6, 68)
      every { mockSubscriptionStorageService.findByRemoteId(1) } returns friends
      every { mockSubscriptionStorageService.findByRemoteId(2) } returns homeland
      val subscriptionBatchService = SubscriptionBatchService(mockSubscriptionStorageService)

      val expectedSubscriptionResult = emptyList<Int>() to listOf(friends, homeland)

      val actualSubscriptionResult = subscriptionBatchService.findByRemoteIds(listOf(1, 2))
      assertThat(actualSubscriptionResult).isEqualTo(expectedSubscriptionResult)
    }
}