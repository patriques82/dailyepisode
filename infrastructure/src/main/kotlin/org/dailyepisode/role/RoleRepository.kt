package org.dailyepisode.role

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<RoleEntity, Long> {
  fun findByRoleName(roleName: String): RoleEntity
}