package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountHasNoMatchingSubscriptionException
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.account.NoAccountFoundException
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesLookupService
import org.dailyepisode.series.SeriesService
import org.springframework.stereotype.Service

@Service
internal class SubscriptionServiceImpl(private val subscriptionRepository: SubscriptionRepository,
                                       private val accountRepository: AccountRepository,
                                       private val seriesService: SeriesService) : SubscriptionService {

  override fun createSubscription(remoteIds: List<Int>, accountId: Long) {
    val accountEntity: AccountEntity? = accountRepository.findById(accountId).orElse(null)
    if (accountEntity == null) {
      throw NoAccountFoundException("No account found for id")
    }
    val subscriptions = getStoredAndCreatedSubscriptions(remoteIds)
    accountEntity.subscriptions += subscriptions.map { it.toEntity() }
    accountRepository.save(accountEntity)
  }

  private fun getStoredAndCreatedSubscriptions(remoteIds: List<Int>): List<Subscription> {
    val (notStoredIds, storedSubscriptions) = SubscriptionLoadService(this).load(remoteIds)
    val seriesLookups = SeriesLookupService(seriesService).lookup(notStoredIds)
    return storedSubscriptions + seriesLookups.map { it.toSubscription() }
  }

  private fun SeriesLookupResult.toSubscription(): Subscription =
    Subscription(null, remoteId, name, overview, imageUrl, voteCount, voteAverage,
      firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons)

  private fun Subscription.toEntity(): SubscriptionEntity =
    SubscriptionEntity(id, remoteId, name, overview, imageUrl, voteCount, voteAverage, firstAirDate, lastAirDate,
      genres, homepage, numberOfEpisodes, numberOfSeasons, emptyList())

  override fun findAll(): List<Subscription> =
    subscriptionRepository.findAll()
      .map { it.toSubscription() }
      .toList()

  override fun findById(subscriptionId: Long): Subscription? =
    subscriptionRepository.findById(subscriptionId)
      .map { it.toSubscription() }
      .orElse(null)

  override fun findByRemoteId(remoteId: Int): Subscription? =
    subscriptionRepository.findByRemoteId(remoteId)
      ?.toSubscription()

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