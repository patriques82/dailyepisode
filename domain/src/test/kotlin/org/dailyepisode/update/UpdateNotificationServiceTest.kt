package org.dailyepisode.update

import io.mockk.spyk
import io.mockk.verify
import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesUpdatedLookupResult
import org.dailyepisode.subscription.Subscription
import org.junit.Test

class UpdateNotificationServiceTest {

  @Test
  fun `send to account with non-overlapping subscriptions and updates should send with empty lookup results`() {
    val notificationSender: NotificationSender = spyk()
    val updateNotificationService = UpdateNotificationService(notificationSender)

    val friends = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15)
    val homeland = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 6, 68)
    val account = Account(1, "dummy", "dummy", "dummy", 0, false, listOf(friends, homeland))

    val seriesLookupResult = SeriesUpdatedLookupResult(3, "newimage", "2017-05-31", 90, 8)

    updateNotificationService.sendTo(account, listOf(seriesLookupResult))

    verify { notificationSender.send(account, emptyList()) }
  }

  @Test
  fun `send to account with overlapping subscriptions and updates should send with overlapping lookup results`() {
    val notificationSender: NotificationSender = spyk()
    val updateNotificationService = UpdateNotificationService(notificationSender)

    val friends = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15)
    val homeland = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 6, 68)
    val theBigBangTheory = Subscription(3, 3, "the big bang theory", "nerds being funny", "image", 30, 6.9, "2010-05-04", "2017-05-05", listOf("Comedy"), null, 89, 8)
    val account = Account(1, "dummy", "dummy", "dummy", 0, false, listOf(friends, homeland, theBigBangTheory))

    val seriesLookupResult = SeriesUpdatedLookupResult(3, "newimage", "2017-05-31", 90, 8)

    updateNotificationService.sendTo(account, listOf(seriesLookupResult))

    verify { notificationSender.send(account, listOf(seriesLookupResult)) }
  }

}