package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.account.AccountHasNoMatchingSubscriptionException
import org.dailyepisode.account.NoAccountFoundException
import org.dailyepisode.series.SeriesService
import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(private val subscriptionRepository: SubscriptionRepository,
                                       private val accountRepository: AccountRepository,
                                       private val seriesService: SeriesService) : SubscriptionService {

  override fun createSubscription(subscription: Subscription, accountId: Long) {
    val accountEntity: AccountEntity? = accountRepository.findById(accountId).orElse(null)
    if (accountEntity == null) {
      throw NoAccountFoundException("No account found for id")
    }
    val subscriptionEntity: SubscriptionEntity? = subscriptionRepository.findByRemoteId(subscription.remoteId)
    if (subscriptionEntity != null) {
      accountEntity.subscriptions += subscriptionEntity
    } else {
      val subscriptionVerifier = SubscriptionVerifier(seriesService)
      subscriptionVerifier.verify(subscription)
      accountEntity.subscriptions += subscription.toEntity()
    }
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
      accountRepository.findById(accountId).orElse(null)
        ?: throw NoAccountFoundException("No account found for id")
    if (!accountEntity.subscribesTo(subscriptionId)) {
      throw AccountHasNoMatchingSubscriptionException("Account has no matching subscription with id")
    }
    accountEntity.subscriptions = accountEntity.subscriptions.filter { it.id != subscriptionId }
    accountRepository.save(accountEntity)
  }

}

