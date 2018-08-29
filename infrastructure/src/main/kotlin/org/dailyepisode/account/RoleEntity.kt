package org.dailyepisode.account

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class RoleEntity(
  @Id
  @GeneratedValue
  val id: Long? = null,

  var roleName: String = "",

  @ManyToMany(mappedBy = "roles")
  var accounts: List<AccountEntity> = emptyList())