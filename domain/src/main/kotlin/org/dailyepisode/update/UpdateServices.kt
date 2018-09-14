package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesBatchService
import org.dailyepisode.series.RemoteSeriesServiceFacade
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

class UpdateFilteringService(private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {

  fun filter(subscriptions: List<Subscription>): List<SeriesLookupResult> {
    val changedIds = remoteSeriesServiceFacade.updatesSinceYesterday().changedSeriesRemoteIds
    val updatedSubscriptions = subscriptions.filter { changedIds.contains(it.remoteId) }
    return lookupUpdates(updatedSubscriptions.map { it.remoteId })
  }

  private fun lookupUpdates(remoteIds: List<Int>): List<SeriesLookupResult> {
    val seriesLookupService = SeriesBatchService(remoteSeriesServiceFacade)
    return seriesLookupService.lookupByRemoteIds(remoteIds)
  }

}

class UpdatePersistService(private val updates: List<SeriesLookupResult>) {
  fun persist(subscription: Subscription) {

  }
}
