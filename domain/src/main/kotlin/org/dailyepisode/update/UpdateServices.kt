package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesLookupService
import org.dailyepisode.series.SeriesService
import org.dailyepisode.subscription.Subscription

interface NotificationSender {
  fun send(account: Account, updates: List<SeriesLookupResult>)
}

class UpdateNotificationService(private val notificationSender: NotificationSender,
                                private val seriesUpdates: List<SeriesLookupResult>) {

  fun notify(account: Account) {
    val subscriptionUpdates = resolveAccountUpdates(account.subscriptions)
    notificationSender.send(account, subscriptionUpdates)
  }

  private fun resolveAccountUpdates(subscriptions: List<Subscription>): List<SeriesLookupResult> {
    val subscriptionRemoteIds = subscriptions.map { it.remoteId }
    return seriesUpdates.filter { subscriptionRemoteIds.contains(it.remoteId) }
  }

}

class UpdateLookupService(private val seriesService: SeriesService,
                          private val subscriptions: List<Subscription>) {

  fun lookup(): List<SeriesLookupResult> {
    val updatedSubscriptions = findAllUpdatedSubscriptions()
    val seriesLookupService = SeriesLookupService(seriesService)
    return seriesLookupService.lookup(updatedSubscriptions.map { it.remoteId })
  }

  private fun findAllUpdatedSubscriptions(): List<Subscription> {
    val changedIds = seriesService.updatesSinceYesterday().changedSeriesRemoteIds
    return subscriptions.filter { changedIds.contains(it.remoteId) }
  }

}
