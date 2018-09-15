package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionStorageService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class ScheduledUpdateNotifier(private val accountStorageService: AccountStorageService,
                              private val subscriptionStorageService: SubscriptionStorageService,
                              private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade,
                              private val notificationSender: NotificationSender) {

  private val logger = LoggerFactory.getLogger(ScheduledUpdateNotifier::class.java)
  private val dateFormat = SimpleDateFormat("EEEEE MMMMM yyyy HH:mm:ss")

  @Scheduled(fixedDelay = 3000)
  fun notifyAndPersistUpdates() {
    logger.info("Notification sending started: {}", dateFormat.format(Date()))

    val subscriptions = subscriptionStorageService.findAll()
    val accounts = accountStorageService.findAll()
    val updateLookupService = UpdateFilteringService(remoteSeriesServiceFacade)
    val updates = updateLookupService.filter(subscriptions)
    notifyUpdates(accounts, updates)
    persistUpdates(subscriptions, updates)

    logger.info("Notification sending ended: {}", dateFormat.format(Date()))
  }

  private fun notifyUpdates(accounts: List<Account>, updates: List<SeriesLookupResult>) {
    val updateNotificationService = UpdateNotificationService(notificationSender, updates)
    accounts.forEach { updateNotificationService.notify(it) }
  }

  private fun persistUpdates(subscriptions: List<Subscription>, updates: List<SeriesLookupResult>) {
    val updatePersistService = UpdatePersistService(updates)
    subscriptions.forEach { updatePersistService.persist(it) }
  }

}
