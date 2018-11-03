package org.dailyepisode.updatenotification

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.UpdatedSeriesResult
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionStorageService
import org.dailyepisode.subscription.SubscriptionUpdateRequest
import org.junit.Test
import java.time.LocalDateTime

class ScheduledUpdateNotifierTest {
  // Dates
  val today = LocalDateTime.now()
  val yesterday = today.minusDays(1)
  val twoDaysAgo = today.minusDays(2)
  val oneWeekAgo = today.minusWeeks(1)
  val oneYearAgo = today.minusYears(1)

  // Subscriptions
  val updatedYesterday = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15, oneYearAgo, yesterday)
  val updatedOneWeekAgo = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 68, 6, oneYearAgo, oneWeekAgo)

  // Accounts
  val notifiable = createAccount(1, 1, listOf(updatedYesterday, updatedOneWeekAgo), twoDaysAgo)
  val nonNotifiableDueToNotificationInterval = createAccount(2, 8, listOf(updatedYesterday, updatedOneWeekAgo), oneWeekAgo)
  val nonNotifiableDueToSubscriptionUpdates = createAccount(3, 1, listOf(updatedOneWeekAgo), twoDaysAgo)

  // SeriesLookupResult
  val updatedYesterdayLookup = SeriesLookupResult(1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2018-11-23", listOf(), "www.friends.com", 103, 15)

  private fun createAccount(id: Long, notificationInterval: Int, subscriptions: List<Subscription>, lastNotification: LocalDateTime) =
    Account(id, "x", "x", "x", notificationInterval, false, subscriptions, oneYearAgo, lastNotification)

  @Test
  fun `notifyAndPersistUpdates notifies the accounts with subscription updates within notification interval`() {
    val accountStorageService= createAccountStorageServiceSpy()
    val subscriptionStorageService= createSubscriptionsStorageServiceSpy()
    val remoteSeriesServiceFacade= mockRemoteSeriesServiceFacade()
    val notificationSender: NotificationSender = spyk()

    val scheduledUpdateNotifier = ScheduledUpdateNotifier(
      accountStorageService,
      subscriptionStorageService,
      remoteSeriesServiceFacade,
      notificationSender)
    scheduledUpdateNotifier.notifyAndPersistUpdates()

    verifyAccountStorageServiceSpy(accountStorageService)
    verifySubscriptionStorageService(subscriptionStorageService)
    verifyNotificationSender(notificationSender)
  }

  private fun createAccountStorageServiceSpy(): AccountStorageService {
    val accountStorageService: AccountStorageService = spyk()
    every { accountStorageService.findAll() } returns listOf(notifiable, nonNotifiableDueToNotificationInterval, nonNotifiableDueToSubscriptionUpdates)
    return accountStorageService
  }

  private fun createSubscriptionsStorageServiceSpy(): SubscriptionStorageService {
    val subscriptionStorageService: SubscriptionStorageService = spyk()
    every { subscriptionStorageService.findAll() } returns listOf(updatedYesterday, updatedOneWeekAgo)
    every { subscriptionStorageService.findByRemoteId(1) } returns updatedYesterday
    every { subscriptionStorageService.findByRemoteId(2) } returns updatedOneWeekAgo
    return subscriptionStorageService
  }

  private fun mockRemoteSeriesServiceFacade(): RemoteSeriesServiceFacade {
    val remoteSeriesServiceFacade: RemoteSeriesServiceFacade = mockk()
    every { remoteSeriesServiceFacade.updatesSinceYesterday() } returns UpdatedSeriesResult(listOf(1))
    every { remoteSeriesServiceFacade.lookup(1) } returns updatedYesterdayLookup
    return remoteSeriesServiceFacade
  }

  private fun verifyAccountStorageServiceSpy(accountStorageService: AccountStorageService) {
    verify { accountStorageService.updateNotifiedAt(notifiable.id, any()) }
    verify(inverse = true) { accountStorageService.updateNotifiedAt(nonNotifiableDueToNotificationInterval.id, any()) }
    verify(inverse = true) { accountStorageService.updateNotifiedAt(nonNotifiableDueToSubscriptionUpdates.id, any()) }
  }

  private fun verifySubscriptionStorageService(subscriptionStorageService: SubscriptionStorageService) {
    val expectedSubscriptionUpdateRequest = SubscriptionUpdateRequest(1, 15)
    verify { subscriptionStorageService.update(expectedSubscriptionUpdateRequest) }
  }

  private fun verifyNotificationSender(notificationSender: NotificationSender) {
    verify { notificationSender.send(notifiable, listOf(updatedYesterday)) }
    verify(inverse = true) { notificationSender.send(nonNotifiableDueToNotificationInterval, any()) }
    verify(inverse = true) { notificationSender.send(nonNotifiableDueToSubscriptionUpdates, any()) }
  }

}