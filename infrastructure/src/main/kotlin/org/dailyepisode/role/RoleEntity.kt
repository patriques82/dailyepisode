package org.dailyepisode.role

import javax.persistence.*

@Entity
data class RoleEntity(
  @Id
  @GeneratedValue
  val id: Long? = null,
  var roleName: String = "",
  var securityLevel: Int)