package org.dailyepisode.subscription

import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(val subscriptionRepository: SubscriptionRepository): SubscriptionService {

  override fun getAll(): List<Subscription> {
    return subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()
  }

  override fun createSubscription(subscription: Subscription): Subscription {
    return subscriptionRepository.save(subscription.toEntity())
      .toSubscription()
  }
}

fun Subscription.toEntity(): SubscriptionEntity {
  return SubscriptionEntity(id, name)
}

fun SubscriptionEntity.toSubscription(): Subscription {
  return Subscription(id, name)
}