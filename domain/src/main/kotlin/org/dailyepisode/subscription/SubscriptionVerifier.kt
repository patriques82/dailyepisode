package org.dailyepisode.subscription

import org.dailyepisode.series.SeriesLookupInfo
import org.dailyepisode.series.SeriesService

class SubscriptionVerifier(val seriesService: SeriesService) {

  fun verify(subscription: Subscription) {
    val seriesLookupInfo: SeriesLookupInfo = seriesService.lookup(subscription.remoteId)
        ?: throw IllegalSubscriptionRemoteIdException("Non existent series id")
    if (seriesLookupInfo.name != subscription.name) {
      throw IllegalSubscriptionNameException("Subscription name: ${subscription.name} is illegal")
    }
    if (seriesLookupInfo.overview != subscription.overview) {
      val croppedOverview = if (subscription.overview.length > 20) {
        "${subscription.overview.subSequence(0,20)}..."
      } else subscription.overview
      throw IllegalSubscriptionOverviewException("Subscription overview: ${croppedOverview}... is illegal")
    }
    if (seriesLookupInfo.imageUrl != subscription.imageUrl) {
      throw IllegalSubscriptionImageUrlException("Subscription image url: ${subscription.imageUrl} is illegal")
    }
  }

}