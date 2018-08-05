package org.dailyepisode.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface AccountRepository: JpaRepository<AccountEntity, Long> {
  fun findByEmail(email: String): AccountEntity?
}
