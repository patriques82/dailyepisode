package org.dailyepisode.update

import org.dailyepisode.account.Account
import org.dailyepisode.series.SeriesUpdatedLookupResult
import org.dailyepisode.subscription.Subscription
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
internal class EmailNotificationSender(private val emailSender: JavaMailSender,
                                       private @Value("\${notification.message.subject}") val messageSubject: String)
  : NotificationSender {

  private val logger = LoggerFactory.getLogger(EmailNotificationSender::class.java)

  override fun send(account: Account, accountUpdates: List<SeriesUpdatedLookupResult>) {
    if (accountUpdates.isNotEmpty()) {

      logger.info("Sending notification email to: {}", account.email)

      val message = SimpleMailMessage()
      message.setTo(account.email)
      message.setSubject(messageSubject)
      message.setText(createMessage(account.subscriptions, accountUpdates))
      emailSender.send(message)
    }
  }

  private fun createMessage(subscriptions: List<Subscription>, updates: List<SeriesUpdatedLookupResult>): String {
    return ""
  }

}