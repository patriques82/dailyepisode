package org.dailyepisode.subscription

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AppSubscriptionService(@Autowired val subscriptionRepository: SubscriptionRepository): SubscriptionService {

  override fun getAll(): List<Subscription> {
    return subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()
  }

  override fun createSubscription(subscription: Subscription): Subscription {
    return subscriptionRepository.save(subscription.toDto())
      .toSubscription()
  }
}

fun Subscription.toDto(): SubscriptionDto {
  return SubscriptionDto(id, name)
}

fun SubscriptionDto.toSubscription(): Subscription {
  return Subscription(id, name)
}