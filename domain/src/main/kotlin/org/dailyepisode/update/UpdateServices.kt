package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.RemoteSeriesLookupBatchService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionStorageService

interface NotificationSender {
  fun send(account: Account, accountUpdates: List<SeriesLookupResult>)
}

class UpdateNotificationService(private val notificationSender: NotificationSender) {

  fun sendTo(account: Account, seriesUpdates: List<SeriesLookupResult>) {
    val accountUpdates = resolveAccountSubscriptionUpdates(account.subscriptions, seriesUpdates)
    notificationSender.send(account, accountUpdates)
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
    return lookupUpdates(updatedSubscriptions.map { it.remoteId })
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
