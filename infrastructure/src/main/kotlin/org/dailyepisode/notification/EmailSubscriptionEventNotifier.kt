package org.dailyepisode.notification

import org.dailyepisode.account.Account
import org.springframework.stereotype.Component

@Component
internal class EmailSubscriptionEventNotifier: SubscriptionEventNotifier {
  override fun notify(account: Account) {

  }

}