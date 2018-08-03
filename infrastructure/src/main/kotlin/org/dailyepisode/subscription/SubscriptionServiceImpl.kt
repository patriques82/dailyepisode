package org.dailyepisode.subscription

import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(val subscriptionRepository: SubscriptionRepository): SubscriptionService {

  override fun createSubscription(subscription: Subscription): Subscription {
    return subscriptionRepository.save(subscription.toEntity())
      .toSubscription()
  }

  override fun getAll(): List<Subscription> {
    return subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()
  }

}

fun Subscription.toEntity(): SubscriptionEntity {
  return SubscriptionEntity(id, remoteId, name, overview, thumbnailUrl)
}

fun SubscriptionEntity.toSubscription(): Subscription {
  return Subscription(id, remoteId, name, overview, thumbnailUrl)
}