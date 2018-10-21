package org.dailyepisode.update

import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.series.toSubscriptionUpdateRequest
import org.dailyepisode.subscription.SubscriptionStorageService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

const val EXECUTION_TIME_CRON_EXPRESSION = "0 0 0 * ? *" // 12:00 AM every day

@Component
class ScheduledUpdateNotifier(private val accountStorageService: AccountStorageService,
                              private val subscriptionStorageService: SubscriptionStorageService,
                              remoteSeriesServiceFacade: RemoteSeriesServiceFacade,
                              notificationSender: NotificationSender) {

  private val logger = LoggerFactory.getLogger(ScheduledUpdateNotifier::class.java)
  private val dateFormat = SimpleDateFormat("EEEEE MMMMM yyyy HH:mm:ss")

  private val updateSearchService = UpdateSearchService(remoteSeriesServiceFacade)
  private val updateNotificationService = UpdateNotificationService(notificationSender, accountStorageService)

  @Scheduled(cron = EXECUTION_TIME_CRON_EXPRESSION)
  fun notifyAndPersistUpdates() {
    logger.info("Notification sending and persisting started: {}", dateFormat.format(Date()))

    val accounts = accountStorageService.findAll()
    val subscriptions = subscriptionStorageService.findAll()
    val updatedSeries = updateSearchService.search(subscriptions)

    updatedSeries.forEach { subscriptionStorageService.update(it.toSubscriptionUpdateRequest()) }
    accounts.forEach { updateNotificationService.notify(it) }

    logger.info("Notification sending and persisting ended: {}", dateFormat.format(Date()))
  }

}
