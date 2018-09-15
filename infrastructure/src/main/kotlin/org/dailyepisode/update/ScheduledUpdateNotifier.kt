package org.dailyepisode.update

import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.dailyepisode.subscription.SubscriptionStorageService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class ScheduledUpdateNotifier(private val accountStorageService: AccountStorageService,
                              private val subscriptionStorageService: SubscriptionStorageService,
                              remoteSeriesServiceFacade: RemoteSeriesServiceFacade,
                              notificationSender: NotificationSender) {

  private val logger = LoggerFactory.getLogger(ScheduledUpdateNotifier::class.java)
  private val dateFormat = SimpleDateFormat("EEEEE MMMMM yyyy HH:mm:ss")
  private val updateSearchService = UpdateSearchService(remoteSeriesServiceFacade)
  private val updateNotificationSendService = UpdateNotificationService(notificationSender)
  private val updatePersistService = UpdatePersistService(subscriptionStorageService)

  @Scheduled(fixedDelay = 3000)
  fun notifyAndPersistUpdates() {
    logger.info("Notification sending started: {}", dateFormat.format(Date()))

    val accounts = accountStorageService.findAll()
    val subscriptions = subscriptionStorageService.findAll()
    val updates = updateSearchService.searchForUpdates(subscriptions)

    accounts.forEach { updateNotificationSendService.sendTo(it, updates) }
    updates.forEach { updatePersistService.persist(it) }

    logger.info("Notification sending ended: {}", dateFormat.format(Date()))
  }

}
