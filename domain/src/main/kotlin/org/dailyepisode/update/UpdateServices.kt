package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.RemoteSeriesLookupBatchService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionStorageService

interface NotificationSender {
  fun send(account: Account, updates: List<SeriesLookupResult>)
}

class UpdateNotificationService(private val notificationSender: NotificationSender) {

  fun send(account: Account, seriesUpdates: List<SeriesLookupResult>) {
    val updates = resolveAccountSubscriptionUpdates(account.subscriptions, seriesUpdates)
    notificationSender.send(account, updates)
  }

  private fun resolveAccountSubscriptionUpdates(subscriptions: List<Subscription>,
                                                seriesUpdates: List<SeriesLookupResult>): List<SeriesLookupResult> {
    val subscriptionRemoteIds = subscriptions.map { it.remoteId }
    return seriesUpdates.filter { subscriptionRemoteIds.contains(it.remoteId) }
  }

}

class UpdateSearchService(private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  fun searchForUpdates(subscriptions: List<Subscription>): List<SeriesLookupResult> {
    val changedRemoteIds = remoteSeriesServiceFacade.updatesSinceYesterday().changedSeriesRemoteIds
    val updatedSubscriptions = subscriptions.filter { changedRemoteIds.contains(it.remoteId) }
    val updates = lookupUpdates(updatedSubscriptions.map { it.remoteId })
    return updates
  }

  private fun lookupUpdates(remoteIds: List<Int>): List<SeriesLookupResult> {
    val seriesLookupService = RemoteSeriesLookupBatchService(remoteSeriesServiceFacade)
    return seriesLookupService.lookup(remoteIds)
  }

}

class UpdatePersistService(private val subscriptionStorageService: SubscriptionStorageService, updates: List<SeriesLookupResult>) {
  fun persist(subscription: Subscription) {

  }
}
