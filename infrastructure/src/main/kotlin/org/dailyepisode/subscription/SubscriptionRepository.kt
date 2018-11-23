package org.dailyepisode.subscription

import org.dailyepisode.account.AccountEntity
import org.dailyepisode.util.toLocalDate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Repository
interface SubscriptionRepository : JpaRepository<SubscriptionEntity, Long> {
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

  @Column(length = 3000)
  var overview: String?,
  var imageUrl: String?,
  var voteCount: Int,
  var voteAverage: Double,
  var firstAirDate: String?,

  var lastAirDate: String?,
  var lastAirDateIsNewSeason: Boolean?,

  @Column
  @ElementCollection
  var genres: List<String>,
  var homepage: String?,
  var numberOfEpisodes: Int,
  var numberOfSeasons: Int,

  @ManyToMany(mappedBy = "subscriptions")
  var accounts: List<AccountEntity>,

  @field: CreationTimestamp
  @field: Column(updatable = false)
  val createdAt: Date = Date(),

  @field: UpdateTimestamp
  val updatedAt: Date = Date(),

  var lastUpdate: Date? = Date(),
  var seasonLastUpdate: Int,

  var nextAirDate: String?,
  var nextAirDateIsNewSeason: Boolean?
) {
  fun toSubscription(): Subscription {
    if (id == null) {
      throw IllegalStateException("Only non transient subscription entities can be transformed to subscriptions")
    }
    return Subscription(id!!, remoteId, name, overview, imageUrl, voteCount, voteAverage, firstAirDate?.toLocalDate(), lastAirDate?.toLocalDate(),
                        lastAirDateIsNewSeason, genres, homepage, numberOfEpisodes, numberOfSeasons, createdAt.toLocalDate(), updatedAt.toLocalDate(),
                        lastUpdate?.toLocalDate(), seasonLastUpdate, nextAirDate?.toLocalDate(), nextAirDateIsNewSeason)
  }
}
