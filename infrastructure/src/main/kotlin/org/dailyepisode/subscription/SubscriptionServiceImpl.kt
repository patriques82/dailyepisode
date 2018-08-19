package org.dailyepisode.subscription

import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(private val subscriptionRepository: SubscriptionRepository) : SubscriptionService {

  override fun createSubscription(subscription: Subscription): Subscription {
    val storedSubscription =
      subscriptionRepository.findByRemoteId(subscription.remoteId) ?:
      subscriptionRepository.save(subscription.toEntity())
    return storedSubscription.toSubscription()
  }

  override fun findAll(): List<Subscription> =
    subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()

  override fun findById(subscriptionId: Long): Subscription? =
    subscriptionRepository.findById(subscriptionId)
      .map { it.toSubscription() }
      .orElse(null)

  private fun SubscriptionEntity.toSubscription(): Subscription =
    Subscription(id, remoteId, name, overview, imageUrl)

  private fun Subscription.toEntity(): SubscriptionEntity =
    SubscriptionEntity(id, remoteId, name, overview, imageUrl, emptyList())

}

