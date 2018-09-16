package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesLookupBatchService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.series.SeriesUpdatedLookupResult
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionStorageService

interface NotificationSender {
  fun send(account: Account, accountUpdates: List<SeriesUpdatedLookupResult>)
}

class UpdateNotificationService(private val notificationSender: NotificationSender) {

  fun sendTo(account: Account, seriesUpdates: List<SeriesUpdatedLookupResult>) {
    val accountUpdates = resolveAccountSubscriptionUpdates(account.subscriptions, seriesUpdates)
    notificationSender.send(account, accountUpdates)
  }

  private fun resolveAccountSubscriptionUpdates(subscriptions: List<Subscription>,
                                                seriesUpdates: List<SeriesUpdatedLookupResult>): List<SeriesUpdatedLookupResult> {
    val subscriptionRemoteIds = subscriptions.map { it.remoteId }
    return seriesUpdates.filter { subscriptionRemoteIds.contains(it.remoteId) }
  }

}

class UpdateSearchService(private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {
  private val seriesLookupBatchService = SeriesLookupBatchService(remoteSeriesServiceFacade)

  fun searchForUpdates(subscriptions: List<Subscription>): List<SeriesUpdatedLookupResult> {
    val changedRemoteIds = remoteSeriesServiceFacade.updatesSinceYesterday().changedSeriesRemoteIds
    val updatedSubscriptions = subscriptions.filter { changedRemoteIds.contains(it.remoteId) }
    val updatedLookups = seriesLookupBatchService.lookup(updatedSubscriptions.map { it.remoteId })
    return updatedLookups.map { it.toSeriesUpdatedLookupResult() }
  }

  private fun SeriesLookupResult.toSeriesUpdatedLookupResult(): SeriesUpdatedLookupResult =
    SeriesUpdatedLookupResult(remoteId, imageUrl, lastAirDate, numberOfEpisodes, numberOfSeasons)

}

class UpdatePersistService(private val subscriptionStorageService: SubscriptionStorageService) {

  fun persist(updatedSeriesLookupResult: SeriesUpdatedLookupResult) {
    val subscription = subscriptionStorageService.findByRemoteId(updatedSeriesLookupResult.remoteId)

  }

}
