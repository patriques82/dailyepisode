package org.dailyepisode.notification

import org.dailyepisode.account.Account

interface SubscriptionEventNotifier {
  fun notify(account: Account)
}