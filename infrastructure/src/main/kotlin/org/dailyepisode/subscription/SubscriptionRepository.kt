package org.dailyepisode.subscription

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface SubscriptionRepository: JpaRepository<SubscriptionEntity, Long> {
  fun findByRemoteId(remoteId: Int): SubscriptionEntity?
}
