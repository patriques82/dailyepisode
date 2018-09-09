package org.dailyepisode.update

import org.dailyepisode.account.AccountService
import org.dailyepisode.series.SeriesService
import org.dailyepisode.subscription.SubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class ScheduledUpdateNotifier(private val accountService: AccountService,
                              private val subscriptionService: SubscriptionService,
                              private val seriesService: SeriesService,
                              private val notificationSender: NotificationSender) {

  private val logger = LoggerFactory.getLogger(ScheduledUpdateNotifier::class.java)
  private val dateFormat = SimpleDateFormat("EEEEE MMMMM yyyy HH:mm:ss")

  @Scheduled(fixedDelay = 3000)
  fun notifyAllUsers() {
    logger.info("Notification sending started: {}", dateFormat.format(Date()))

    val subscriptions = subscriptionService.findAll()
    val updates = UpdateLookupService(seriesService, subscriptions).lookup()
    val updateNotifier = UpdateNotificationService(notificationSender, updates)
    val accounts = accountService.findAll()
    accounts.forEach {
      updateNotifier.notify(it)
    }

    logger.info("Notification sending ended: {}", dateFormat.format(Date()))
  }

}