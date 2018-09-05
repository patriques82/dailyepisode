package org.dailyepisode.subscription

import org.dailyepisode.series.SeriesLookupInfo
import org.dailyepisode.series.SeriesService

class SubscriptionResolver(val seriesService: SeriesService) {

  fun resolve(subscriptionRequest: SubscriptionRequest): Subscription {
    val seriesLookupInfo: SeriesLookupInfo? = seriesService.lookup(subscriptionRequest.remoteId)
    if (seriesLookupInfo == null) {
      throw IllegalSubscriptionRemoteIdException("Non existent series id")
    }
    verify(seriesLookupInfo, subscriptionRequest)
    return seriesLookupInfo.toSubscription()
  }

  private fun SeriesLookupInfo.toSubscription(): Subscription =
    Subscription(null, remoteId, name, overview, imageUrl, voteCount, voteAverage,
      firstAirDate, lastAirDate, genres, homepage, numberOfEpisodes, numberOfSeasons)

  private fun verify(seriesLookupInfo: SeriesLookupInfo, subscriptionRequest: SubscriptionRequest) {
    if (seriesLookupInfo.name != subscriptionRequest.name) {
      throw IllegalSubscriptionNameException("Illegal subscription name: ${subscriptionRequest.name}")
    }
    if (seriesLookupInfo.overview != subscriptionRequest.overview) {
      when(subscriptionRequest.overview?.length) {
        null -> throw IllegalSubscriptionOverviewException("Illegal subscription overview")
        in 0..20 -> throw IllegalSubscriptionOverviewException("Illegal subscription overview: ${subscriptionRequest.overview}")
        else -> throw IllegalSubscriptionOverviewException("Illegal subscription overview: ${subscriptionRequest.overview.subSequence(0,20)}...")
      }
    }
    if (seriesLookupInfo.imageUrl != subscriptionRequest.imageUrl) {
      throw IllegalSubscriptionImageUrlException("Illegal subscription image url: ${subscriptionRequest.imageUrl}")
    }
  }

}