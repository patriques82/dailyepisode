package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Repository
interface SubscriptionRepository: JpaRepository<SubscriptionEntity, Long> {
  fun findByRemoteId(remoteId: Int): SubscriptionEntity?
}

@Entity
@Table(name = "subscription")
data class SubscriptionEntity(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  var remoteId: Int,
  var name: String,

  @Column(length = 1000)
  var overview: String?,
  var imageUrl: String?,
  var voteCount: Int,
  var voteAverage: Double,
  var firstAirDate: String?,
  var lastAirDate: String?,

  @Column
  @ElementCollection
  var genres: List<String>,
  var homepage: String?,
  var numberOfEpisodes: Int,
  var numberOfSeasons: Int,

  @ManyToMany(mappedBy = "subscriptions")
  var accounts: List<AccountEntity>
) {
  fun toSubscription(): Subscription =
    Subscription(id, remoteId, name, overview, imageUrl, voteCount, voteAverage, firstAirDate, lastAirDate,
      genres, homepage, numberOfEpisodes, numberOfSeasons)
}
