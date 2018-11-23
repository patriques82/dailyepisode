package org.dailyepisode.updatenotification

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionStorageService
import org.dailyepisode.util.toLocalDate
import org.junit.Test
import java.time.LocalDate

class ScheduledUpdateNotifierTest {
  // Dates
  val today = LocalDate.now()
  val tomorrow = today.plusDays(1)
  val yesterday = today.minusDays(1)
  val twoDaysAgo = today.minusDays(2)
  val oneWeekAgo = today.minusWeeks(1)
  val oneYearAgo = today.minusYears(1)

  // Subscriptions
  val newSeasonTomorrowSubscription = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17".toLocalDate(), "2014-10-22".toLocalDate(), false, listOf(), "www.friends.com", 103, 15, oneYearAgo, yesterday, twoDaysAgo, 15, tomorrow, true)
  val newSeasonAfterLastUpdateSubcription = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23".toLocalDate(), "2017-08-12".toLocalDate(), true, listOf(), "www.homeland.com", 68, 6, oneYearAgo, today, "2017-08-10".toLocalDate(), 5, null, null)
  val noNewSeasonSubscription = Subscription(3, 3, "airwolf", "amazing helicopter", "image", 44, 9.5, "2008-01-23".toLocalDate(), "2017-08-12".toLocalDate(), false, listOf(), "www.airwolf.com", 68, 6, oneYearAgo, yesterday, twoDaysAgo, 6, null, null)

  // Accounts
  val notifiable = createAccount(1, 1, listOf(newSeasonTomorrowSubscription, newSeasonAfterLastUpdateSubcription), twoDaysAgo)
  val nonNotifiableDueSubscriptions = createAccount(2, 1, listOf(noNewSeasonSubscription), twoDaysAgo)
  val nonNotifiableDueToNotificationInterval = createAccount(3, 8, listOf(newSeasonTomorrowSubscription, newSeasonAfterLastUpdateSubcription), oneWeekAgo)

  private fun createAccount(id: Long, notificationInterval: Int, subscriptions: List<Subscription>, lastNotification: LocalDate) =
    Account(id, "x", "x", "x", notificationInterval, false, subscriptions, oneYearAgo, lastNotification)

  @Test
  fun `notifyAndPersistUpdates notifies the accounts with subscription updates within notification interval`() {
    val accountStorageService= createAccountStorageServiceSpy()
    val subscriptionStorageService = mockSubscriptionsStorageService()
    val remoteSeriesServiceFacade= mockRemoteSeriesServiceFacade()
    val notificationSender: NotificationSender = spyk()

    val scheduledUpdateNotifier = ScheduledUpdateNotifier(
      accountStorageService,
      subscriptionStorageService,
      remoteSeriesServiceFacade,
      notificationSender)
    scheduledUpdateNotifier.notifyAndPersistUpdates()

    verifyAccountStorageServiceSpy(accountStorageService)
    verifyNotificationSender(notificationSender)
  }

  private fun createAccountStorageServiceSpy(): AccountStorageService {
    val accountStorageService: AccountStorageService = spyk()
    every { accountStorageService.findAll() } returns listOf(notifiable, nonNotifiableDueSubscriptions, nonNotifiableDueToNotificationInterval)
    return accountStorageService
  }

  private fun mockSubscriptionsStorageService(): SubscriptionStorageService {
    val subscriptionStorageService: SubscriptionStorageService = spyk()
    every { subscriptionStorageService.findAll() } returns listOf(newSeasonTomorrowSubscription, newSeasonAfterLastUpdateSubcription, noNewSeasonSubscription)
    every { subscriptionStorageService.findByRemoteId(1) } returns newSeasonTomorrowSubscription
    every { subscriptionStorageService.findByRemoteId(2) } returns newSeasonAfterLastUpdateSubcription
    every { subscriptionStorageService.findByRemoteId(3) } returns noNewSeasonSubscription
    every { subscriptionStorageService.update(any()) }
    return subscriptionStorageService
  }

  private fun mockRemoteSeriesServiceFacade(): RemoteSeriesServiceFacade {
    val dummy = SeriesLookupResult(1, "", "", "", 0, 0.0, "", "", false, listOf(), "", 0, 0, "", null)
    val remoteSeriesServiceFacade: RemoteSeriesServiceFacade = mockk()
    every { remoteSeriesServiceFacade.lookup(any()) } returns dummy
    return remoteSeriesServiceFacade
  }

  private fun verifyAccountStorageServiceSpy(accountStorageService: AccountStorageService) {
    verify { accountStorageService.updateNotifiedAt(notifiable.id, any()) }
    verify(inverse = true) { accountStorageService.updateNotifiedAt(nonNotifiableDueSubscriptions.id, any()) }
    verify(inverse = true) { accountStorageService.updateNotifiedAt(nonNotifiableDueToNotificationInterval.id, any()) }
  }

  private fun verifyNotificationSender(notificationSender: NotificationSender) {
    verify { notificationSender.send(notifiable, listOf(newSeasonTomorrowSubscription, newSeasonAfterLastUpdateSubcription)) }
    verify(inverse = true) { notificationSender.send(nonNotifiableDueSubscriptions, any()) }
    verify(inverse = true) { notificationSender.send(nonNotifiableDueToNotificationInterval, any()) }
  }

}