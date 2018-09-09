package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.subscription.Subscription
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
internal class EmailNotificationSender(private val emailSender: JavaMailSender,
                                       private @Value("\${notification.message.subject}") val messageSubject: String): NotificationSender {

  private val logger = LoggerFactory.getLogger(EmailNotificationSender::class.java)

  override fun send(account: Account, updates: List<SeriesLookupResult>) {
    if (updates.isNotEmpty()) {

      logger.info("Sending notification email to: {}", account.email)

      val message = SimpleMailMessage()
      message.setTo(account.email)
      message.setSubject(messageSubject)
      message.setText(createMessage(account.subscriptions, updates))
      emailSender.send(message)

    }
  }

  private fun createMessage(subscriptions: List<Subscription>, updates: List<SeriesLookupResult>): String {
    return ""
  }

}