package org.dailyepisode.update

import io.mockk.spyk
import io.mockk.verify
import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.subscription.Subscription
import org.junit.Test
import java.time.LocalDateTime

class UpdateNotificationServiceTest {
  val today = LocalDateTime.now()
  val yesterday = today.minusDays(1)
  val twoDaysAgo = today.minusDays(2)
  val oneWeekAgo = today.minusWeeks(1)
  val oneYearAgo = today.minusYears(1)
  val updatedSubscription = Subscription(1, 1, "friends", "friends in appartment", "image", 32, 7.5, "2005-01-17", "2014-10-22", listOf(), "www.friends.com", 103, 15, oneYearAgo, yesterday)
  val nonUpdatedSubscription = Subscription(2, 2, "homeland", "terrorist chasing carrie", "image", 44, 9.5, "2008-01-23", "2017-08-12", listOf(), "www.homeland.com", 6, 68, oneYearAgo, oneWeekAgo)

  @Test
  fun `notify account with updated subscriptions should send the updated subscriptions`() {
    val notificationSender: NotificationSender = spyk()
    val accountStorageService: AccountStorageService = spyk()
    val updateNotificationService = UpdateNotificationService(notificationSender, accountStorageService)
    val notifiableAccount = Account(1, "dummy", "dummy", "dummy", 1, false, listOf(updatedSubscription, nonUpdatedSubscription), oneYearAgo, twoDaysAgo)

    updateNotificationService.notify(notifiableAccount)

    verify { notificationSender.send(notifiableAccount, listOf(updatedSubscription)) }
    verify { accountStorageService.updateNotifiedAt(notifiableAccount.id, any()) }
  }

  @Test
  fun `notify account with no updated subscriptions should not send anything`() {
    val notificationSender: NotificationSender = spyk()
    val accountStorageService: AccountStorageService = spyk()
    val updateNotificationService = UpdateNotificationService(notificationSender, accountStorageService)
    val nonNotifiableAccount = Account(1, "dummy", "dummy", "dummy", 1, false, listOf(nonUpdatedSubscription), oneYearAgo, yesterday)

    updateNotificationService.notify(nonNotifiableAccount)

    verify(inverse = true) { accountStorageService.updateNotifiedAt(nonNotifiableAccount.id, any()) }
  }

}