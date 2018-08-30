package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(private val subscriptionRepository: SubscriptionRepository,
                                       private val accountRepository: AccountRepository) : SubscriptionService {

  override fun createSubscription(subscription: Subscription, accountId: Long) {
    val subscriptionEntity = subscriptionRepository.findByRemoteId(subscription.remoteId) ?: subscription.toEntity()
    val account: AccountEntity = accountRepository.findById(accountId).get()
    account.subscriptions += subscriptionEntity
    accountRepository.save(account)
  }

  override fun findAll(): List<Subscription> =
    subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()

  override fun findById(subscriptionId: Long): Subscription? =
    subscriptionRepository.findById(subscriptionId)
      .map { it.toSubscription() }
      .orElse(null)

  private fun Subscription.toEntity(): SubscriptionEntity =
    SubscriptionEntity(id, remoteId, name, overview, imageUrl, emptyList())

}

