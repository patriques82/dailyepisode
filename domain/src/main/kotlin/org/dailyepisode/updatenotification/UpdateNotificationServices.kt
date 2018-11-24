package org.dailyepisode.updatenotification

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.*
import org.dailyepisode.subscription.Subscription
import java.time.LocalDate
import java.util.*

interface NotificationSender {
  fun send(account: Account, updatedSubscriptions: List<Subscription>)
}

class NotificationService(private val notificationSender: NotificationSender,
                          private val accountStorageService: AccountStorageService) {

  fun notify(account: Account) {
    val updatedSubscriptions = account.subscriptions.filter { it.hasNewSeason(account.notifiedAt) }
    if (!updatedSubscriptions.isEmpty() && isTimeForNotification(account.notifiedAt, account.notificationIntervalInDays)) {
      notificationSender.send(account, updatedSubscriptions)
      accountStorageService.updateNotifiedAt(account.id, Date())
    }
  }

  private fun isTimeForNotification(lastNotificationDate: LocalDate, notificationIntervalInDays: Int): Boolean =
    lastNotificationDate.plusDays(notificationIntervalInDays.toLong()).isBefore(LocalDate.now().plusDays(1))

  private fun Subscription.hasNewSeason(notifiedAt: LocalDate): Boolean {
    var newSeason = false
    if (lastAirDate != null && lastAirDateIsNewSeason != null) {
      if (newSeasonHasBeenReleasedSinceLastNotification(lastAirDate, notifiedAt, lastAirDateIsNewSeason)) {
        newSeason = true
      }
    }
    if (nextAirDate != null && nextAirDateIsNewSeason != null) {
      if (newSeasonTomorrow(nextAirDate, nextAirDateIsNewSeason)) {
        newSeason = true
      }
    }
    return newSeason
  }

  private fun newSeasonTomorrow(nextAirDate: LocalDate, nextAirDateIsNewSeason: Boolean) =
    nextAirDate.equals(LocalDate.now().plusDays(1)) && nextAirDateIsNewSeason

  private fun newSeasonHasBeenReleasedSinceLastNotification(lastAirDate: LocalDate, notifiedAt: LocalDate, lastAirDateIsNewSeason: Boolean) =
    lastAirDate.minusDays(1).isAfter(notifiedAt) && lastAirDateIsNewSeason
}

class UpdateSearchService(remoteSeriesServiceFacade: RemoteSeriesServiceFacade) {
  private val seriesLookupBatchService = SeriesLookupBatchService(remoteSeriesServiceFacade)

  fun search(subscriptions: List<Subscription>): List<SeriesUpdatedLookupResult> {
    val updatedLookups = seriesLookupBatchService.lookup(subscriptions.map { it.remoteId })
    return updatedLookups.map { it.toSeriesUpdatedLookupResult() }
  }

}