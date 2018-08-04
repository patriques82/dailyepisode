package org.dailyepisode.subscription

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountEntity
import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(val subscriptionRepository: SubscriptionRepository): SubscriptionService {

  override fun createSubscription(subscription: Subscription): Subscription {
    return subscriptionRepository.save(subscription.toEntity())
      .subscription
  }

  override fun getAll(): List<Subscription> {
    return subscriptionRepository.findAll()
      .map { it.subscription }
      .toList()
  }

}

fun Account.toEntity(): AccountEntity {
  return AccountEntity(id, username, email, password, subscriptions.map { it.toEntity() })
}

fun Subscription.toEntity(): SubscriptionEntity {
  return SubscriptionEntity(id, remoteId, name, overview, imageUrl, accounts.map { it.toEntity() })
}
