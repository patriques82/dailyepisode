package org.dailyepisode.subscription

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
internal interface SubscriptionRepository: CrudRepository<SubscriptionEntity, Long>
