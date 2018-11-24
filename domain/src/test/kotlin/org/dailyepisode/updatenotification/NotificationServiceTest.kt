package org.dailyepisode.updatenotification

import io.mockk.spyk
import io.mockk.verify
import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.util.toLocalDate
import org.junit.Test
import java.time.LocalDate

class NotificationServiceTest {
  val today = LocalDate.now()
  val tomorrow = today.plusDays(1)
  val yesterday = today.minusDays(1)
  val twoDaysAgo = today.minusDays(2)
  val oneYearAgo = today.minusYears(1)

  val newSeasonTomorrowSubscription = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17".toLocalDate(), "2014-10-22".toLocalDate(), false, listOf(), "www.friends.com", 103, 15, oneYearAgo, yesterday, twoDaysAgo, 15, tomorrow, true)
  val newSeasonAfterLastNotificationSubcription = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23".toLocalDate(), today, true, listOf(), "www.homeland.com", 68, 6, oneYearAgo, today, "2017-08-10".toLocalDate(), 5, null, null)
  val noNewSeasonSubscription = Subscription(3, 3, "airwolf", "amazing helicopter", "image", 44, 9.5, "2008-01-23".toLocalDate(), "2017-08-12".toLocalDate(), false, listOf(), "www.airwolf.com", 68, 6, oneYearAgo, yesterday, twoDaysAgo, 6, null, null)

  @Test
  fun `notify account with updated subscriptions should send the updated subscriptions`() {
    val notificationSender: NotificationSender = spyk()
    val accountStorageService: AccountStorageService = spyk()
    val updateNotificationService = NotificationService(notificationSender, accountStorageService)
    val notifiableAccount = Account(1, "dummy", "dummy", "dummy", 1, false, listOf(newSeasonTomorrowSubscription, newSeasonAfterLastNotificationSubcription, noNewSeasonSubscription), oneYearAgo, twoDaysAgo)

    updateNotificationService.notify(notifiableAccount)

    verify { notificationSender.send(notifiableAccount, listOf(newSeasonTomorrowSubscription, newSeasonAfterLastNotificationSubcription)) }
    verify { accountStorageService.updateNotifiedAt(notifiableAccount.id, any()) }
  }

  @Test
  fun `notify account with no updated subscriptions should not send anything`() {
    val notificationSender: NotificationSender = spyk()
    val accountStorageService: AccountStorageService = spyk()
    val updateNotificationService = NotificationService(notificationSender, accountStorageService)
    val nonNotifiableAccount = Account(1, "dummy", "dummy", "dummy", 1, false, listOf(noNewSeasonSubscription), oneYearAgo, yesterday)

    updateNotificationService.notify(nonNotifiableAccount)

    verify(inverse = true) { accountStorageService.updateNotifiedAt(nonNotifiableAccount.id, any()) }
  }

}