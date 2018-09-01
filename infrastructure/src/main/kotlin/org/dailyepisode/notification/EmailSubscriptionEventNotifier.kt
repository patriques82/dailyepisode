package org.dailyepisode.notification

import org.dailyepisode.subscription.SubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class EmailSubscriptionEventNotifier(val subscriptionService: SubscriptionService) {

  private val log = LoggerFactory.getLogger(EmailSubscriptionEventNotifier::class.java)
  private val dateFormat = SimpleDateFormat("HH:mm:ss")

  @Scheduled(fixedRate = 5000)
  fun reportCurrentTime() {
    log.info("The time is now {}", dateFormat.format(Date()))
  }

}