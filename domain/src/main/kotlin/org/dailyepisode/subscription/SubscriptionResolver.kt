package org.dailyepisode.subscription

import org.dailyepisode.series.SeriesLookupInfo
import org.dailyepisode.series.SeriesService

class SubscriptionResolver(val seriesService: SeriesService) {

  fun resolve(subscriptionRequest: SubscriptionRequest): Subscription {
    val seriesLookupInfo: SeriesLookupInfo? = seriesService.lookup(subscriptionRequest.remoteId)
    if (seriesLookupInfo == null) {
      throw SubscriptionRemoteIdNullPointerException("Non existent series id")
    }
    verify(seriesLookupInfo, subscriptionRequest)
    return seriesLookupInfo.toSubscription()
  }

  private fun SeriesLookupInfo.toSubscription(): Subscription =
    Subscription(null, remoteId, name, overview, imageUrl, voteCount, voteAverage,
      firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons)

  private fun verify(seriesLookupInfo: SeriesLookupInfo, subscriptionRequest: SubscriptionRequest) {
    if (seriesLookupInfo.name != subscriptionRequest.name) {
      throw ConflictingSubscriptionNameException("Illegal subscription name: ${subscriptionRequest.name}")
    }
    if (seriesLookupInfo.overview != subscriptionRequest.overview) {
      when(subscriptionRequest.overview?.length) {
        null -> throw ConflictingSubscriptionOverviewException("Illegal subscription overview")
        in 0..20 -> throw ConflictingSubscriptionOverviewException("Illegal subscription overview: ${subscriptionRequest.overview}")
        else -> throw ConflictingSubscriptionOverviewException("Illegal subscription overview: ${subscriptionRequest.overview.subSequence(0,20)}...")
      }
    }
    if (seriesLookupInfo.imageUrl != subscriptionRequest.imageUrl) {
      throw ConflictingSubscriptionImageUrlException("Illegal subscription image url: ${subscriptionRequest.imageUrl}")
    }
  }

}