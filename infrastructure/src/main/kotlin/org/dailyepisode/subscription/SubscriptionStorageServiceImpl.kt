package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.account.AccountHasNoMatchingSubscriptionException
import org.dailyepisode.account.AccountRepository
import org.dailyepisode.account.NoAccountFoundException
import org.dailyepisode.series.SeriesLookupResult
import org.dailyepisode.series.SeriesBatchService
import org.dailyepisode.series.RemoteSeriesServiceFacade
import org.springframework.stereotype.Service

@Service
internal class SubscriptionStorageServiceImpl(private val subscriptionRepository: SubscriptionRepository,
                                              private val accountRepository: AccountRepository,
                                              private val remoteSeriesServiceFacade: RemoteSeriesServiceFacade) : SubscriptionStorageService {

  override fun createSubscriptions(createSubscriptionsRequest: CreateSubscriptionsRequest) {
    val accountEntity: AccountEntity? = accountRepository.findById(createSubscriptionsRequest.accountId).orElse(null)
    if (accountEntity == null) {
      throw NoAccountFoundException("No account found for id")
    }
    val (notStoredIds, storedSubscriptions) = SubscriptionBatchService(this).findByRemoteIds(createSubscriptionsRequest.remoteIds)
    accountEntity.subscriptions += storedSubscriptions.map { it.toEntity() }
    val seriesLookups = SeriesBatchService(remoteSeriesServiceFacade).lookupByRemoteIds(notStoredIds)
    accountEntity.subscriptions += seriesLookups.map { it.toSubscriptionEntity() }
    accountRepository.save(accountEntity)
  }

  private fun SeriesLookupResult.toSubscriptionEntity(): SubscriptionEntity =
    SubscriptionEntity(null, remoteId, name, overview, imageUrl, voteCount, voteAverage,
      firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons, emptyList())

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

  override fun deleteSubscription(deleteSubscriptionRequest: DeleteSubscriptionRequest) {
    val accountEntity: AccountEntity? = accountRepository.findById(deleteSubscriptionRequest.accountId).orElse(null)
    if (accountEntity == null) {
      throw NoAccountFoundException("No account found for id")
    }
    if (!accountEntity.subscribesTo(deleteSubscriptionRequest.subscriptionId)) {
      throw AccountHasNoMatchingSubscriptionException("Account has no matching subscription with id")
    }
    accountEntity.subscriptions = accountEntity.subscriptions.filter { it.id != deleteSubscriptionRequest.subscriptionId }
    accountRepository.save(accountEntity)
  }

}