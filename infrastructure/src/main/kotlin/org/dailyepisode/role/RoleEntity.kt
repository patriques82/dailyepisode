package org.dailyepisode.role

import org.dailyepisode.account.AccountEntity
import javax.persistence.*

@Entity
data class RoleEntity(
  @Id
  @GeneratedValue
  val id: Long? = null,

  var roleName: String = "",
  var securityLevel: Int,

  @ManyToMany(mappedBy = "roles")
  var accounts: List<AccountEntity> = emptyList())