package org.dailyepisode.notification

import org.dailyepisode.account.AccountService
import org.dailyepisode.subscription.Subscription
import org.dailyepisode.subscription.SubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class ScheduledAccountNotifier(private val accountService: AccountService,
                               private val subscriptionService: SubscriptionService,
                               private val subscriptionEventNotifier: SubscriptionEventNotifier) {

  private val logger = LoggerFactory.getLogger(ScheduledAccountNotifier::class.java)
  private val dateFormat = SimpleDateFormat("EEEEE MMMMM yyyy HH:mm:ss", Locale("sv", "SE"))

  @Scheduled(fixedDelay = 3000)
  fun notifyUsers() {
    logger.info("Notification sending started: {}", dateFormat.format(Date()))
    val subscriptions = subscriptionService.findAll()
    val updates = subscriptions.map { fetchUpdates(it) }
    val accounts = accountService.findAll()
//    accountService.findAll().forEach { subscriptionEventNotifier.notify(it) }
    logger.info("Notification sending ended: {}", dateFormat.format(Date()))
  }

  private fun fetchUpdates(subscription: Subscription): SubscriptionUpdate {
    return SubscriptionUpdate.Modified("test")
  }

}

sealed class SubscriptionUpdate {
  data class Modified(val test: String) : SubscriptionUpdate()
  data class Unmodified(val t: Int) : SubscriptionUpdate()
}


