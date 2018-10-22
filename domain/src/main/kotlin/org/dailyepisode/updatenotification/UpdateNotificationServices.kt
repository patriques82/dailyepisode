package org.dailyepisode.updatenotification

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.*
import org.dailyepisode.subscription.Subscription
import java.time.LocalDateTime
import java.util.*

interface NotificationSender {
  fun send(account: Account, updatedSubscriptions: List<Subscription>)
}

class NotificationService(private val notificationSender: NotificationSender,
                          private val accountStorageService: AccountStorageService) {

  fun notify(account: Account) {
    val updatedSubscriptions = findUpdatedSubscriptions(account.notifiedAt, account.subscriptions)
    if (!updatedSubscriptions.isEmpty() && isTimeForUpdate(account.notifiedAt, account.notificationIntervalInDays)) {
      notificationSender.send(account, updatedSubscriptions)
      accountStorageService.updateNotifiedAt(account.id, Date())
    }
  }

  private fun findUpdatedSubscriptions(lastNotificationDate: LocalDateTime, subscriptions: List<Subscription>) =
    subscriptions.filter { it.updatedAt.isAfter(lastNotificationDate) }

  private fun isTimeForUpdate(lastNotificationDate: LocalDateTime, notificationIntervalInDays: Int): Boolean =
    lastNotificationDate.plusDays(notificationIntervalInDays.toLong()).isBefore(LocalDateTime.now())
}

class UpdateSearchService(private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {
  private val seriesLookupBatchService = SeriesLookupBatchService(remoteSeriesServiceFacade)

  fun search(subscriptions: List<Subscription>): List<SeriesUpdatedLookupResult> {
    val changedRemoteIds = remoteSeriesServiceFacade.updatesSinceYesterday().changedSeriesRemoteIds
    val updatedSubscriptions = subscriptions.filter { changedRemoteIds.contains(it.remoteId) }
    val updatedLookups = seriesLookupBatchService.lookup(updatedSubscriptions.map { it.remoteId })
    return updatedLookups.map { it.toSeriesUpdatedLookupResult() }
  }

}