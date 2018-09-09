package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesService
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionService

interface NotificationSender {
  fun send(account: Account, updates: List<SeriesLookupResult>)
}

class UpdateNotifier(private val seriesService: SeriesService,
                     private val subscriptionService: SubscriptionService,
                     private val notificationSender: NotificationSender) {

  private var seriesUpdates: List<SeriesLookupResult>

  init {
    seriesUpdates = lookupSeriesUpdates() // TODO get as parameter (move logic to other class: UpdateFinder)
  }

  fun notify(account: Account) {
    val subscriptionUpdates = resolveAccountUpdates(account.subscriptions)
    notificationSender.send(account, subscriptionUpdates)
    //TODO store subscription updates in account
  }

  private fun lookupSeriesUpdates(): List<SeriesLookupResult> {
    val updatedSubscriptions = findAllUpdatedSubscriptions()
    val lookups = mutableListOf<SeriesLookupResult>()
    updatedSubscriptions.forEach {
      val lookupResult = seriesService.lookup(it.remoteId)
      if (lookupResult != null) {
        lookups += lookupResult
      }
    }
    return lookups
  }

  private fun findAllUpdatedSubscriptions(): List<Subscription> {
    val changedIds = seriesService.updatesSinceYesterday().changedSeriesRemoteIds
    val subscriptions = subscriptionService.findAll()
    return subscriptions.filter { changedIds.contains(it.remoteId) }
  }

  private fun resolveAccountUpdates(subscriptions: List<Subscription>): List<SeriesLookupResult> {
    val subscriptionRemoteIds = subscriptions.map { it.remoteId }
    return seriesUpdates.filter { subscriptionRemoteIds.contains(it.remoteId) }
  }

}
