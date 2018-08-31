package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.exception.AccountHasNoMatchingSubscriptionException
import org.dailyepisode.exception.NoAccountFoundException
import org.dailyepisode.exception.NoSubscriptionFoundException
import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(private val subscriptionRepository: SubscriptionRepository,
                                       private val accountRepository: AccountRepository) : SubscriptionService {

  override fun createSubscription(subscription: Subscription, accountId: Long) {
    val subscriptionEntity: SubscriptionEntity = subscriptionRepository.findByRemoteId(subscription.remoteId) ?: subscription.toEntity()
    val accountEntity: AccountEntity =
      accountRepository.findById(accountId).orElse(null) ?:
      throw NoAccountFoundException("No account found for id: $accountId")
    accountEntity.subscriptions += subscriptionEntity
    accountRepository.save(accountEntity)
  }

  private fun Subscription.toEntity(): SubscriptionEntity =
    SubscriptionEntity(id, remoteId, name, overview, imageUrl, emptyList())

  override fun findAll(): List<Subscription> =
    subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()

  override fun findById(subscriptionId: Long): Subscription? =
    subscriptionRepository.findById(subscriptionId)
      .map { it.toSubscription() }
      .orElse(null)

  override fun deleteSubscription(subscriptionId: Long, accountId: Long) {
    val accountEntity: AccountEntity =
      accountRepository.findById(accountId).orElse(null) ?:
      throw NoAccountFoundException("No account found for id: $accountId")
    if (!accountEntity.subscribesTo(subscriptionId)) {
      throw AccountHasNoMatchingSubscriptionException("Account has no matching subscription with id: $subscriptionId")
    }
    accountEntity.subscriptions = accountEntity.subscriptions.filter { it.id != subscriptionId }
    accountRepository.save(accountEntity)
  }

}

