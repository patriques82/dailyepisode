package org.dailyepisode.security

import org.dailyepisode.account.AccountEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

internal object UserDetailsFactory {
  fun create(accountEntity: AccountEntity?): UserDetails {
    return accountEntity?.let {
      NON_AUTHORIZED_USER_DETAILS
    } ?: NON_AUTHORIZED_USER_DETAILS
  }
}

object NON_AUTHORIZED_USER_DETAILS: UserDetails {
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()
  override fun isEnabled()= false
  override fun getUsername()= ""
  override fun isCredentialsNonExpired()= false
  override fun getPassword() = ""
  override fun isAccountNonExpired() = false
  override fun isAccountNonLocked() = false
}

