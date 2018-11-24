package org.dailyepisode.updatenotification

import org.dailyepisode.account.Account
import org.dailyepisode.subscription.Subscription
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import org.springframework.mail.javamail.MimeMessageHelper
import java.nio.charset.StandardCharsets
import freemarker.template.Configuration
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import javax.mail.internet.MimeMessage

@Component
internal class EmailNotificationSender(private val emailSender: JavaMailSender,
                                       private val freemarkerConfig: Configuration,
                                       private @Value("\${notification.message.subject}") val messageSubject: String)
  : NotificationSender {

  private val logger = LoggerFactory.getLogger(EmailNotificationSender::class.java)

  override fun send(account: Account, updatedSubscriptions: List<Subscription>) {
    logger.info("Sending notification email to: {}", account.email)
    val message = createMimeMessage(account, updatedSubscriptions)
    emailSender.send(message)
  }

  private fun createMimeMessage(account: Account, updatedSubscriptions: List<Subscription>): MimeMessage {
    val message = emailSender.createMimeMessage()
    val helper = MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name())
    val html = htmlMessage(account, updatedSubscriptions)
    helper.setTo(account.email)
    helper.setText(html, true)
    helper.setSubject(messageSubject)
    helper.setFrom("no-reply@dailyepisode.se")
    return message
  }

  private fun htmlMessage(account: Account, updatedSubscriptions: List<Subscription>): String {
    val template = freemarkerConfig.getTemplate("email-template.ftlh")
    val html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mapOf(
      "username" to account.username,
      "nrOfUpdates" to updatedSubscriptions.size,
      "subscriptions" to updatedSubscriptions.map { it.toEmailSubscriptionModel() }
    ))
    return html
  }

}

internal fun Subscription.toEmailSubscriptionModel(): EmailSubscriptionModel =
  EmailSubscriptionModel(
    name, imageUrl, firstAirDate?.toString(), lastAirDate?.toString(), homepage, numberOfEpisodes, numberOfSeasons,
    updatedAt.toString(), nextAirDate?.toString()
  )

internal data class EmailSubscriptionModel(
  val name: String,
  val imageUrl: String?,
  val firstAirDate: String?,
  val lastAirDate: String?,
  val homepage: String?,
  val numberOfEpisodes: Int,
  val numberOfSeasons: Int,
  val updatedAt: String?,
  val nextAirDate: String?
)