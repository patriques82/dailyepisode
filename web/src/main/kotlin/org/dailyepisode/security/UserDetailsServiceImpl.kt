package org.dailyepisode.security

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
internal class UserDetailsServiceImpl(private val accountService: AccountService) : UserDetailsService {

  override fun loadUserByUsername(username: String?): UserDetails {
    val account = username?.let { accountService.findByUserName(it) }
    if (account == null) {
      throw UsernameNotFoundException("Username: '$username' does not exists")
    }
    return createUserDetails(account)
  }

  private fun createUserDetails(account: Account): UserDetails {
    return object : UserDetails {
      override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val grantedAuthorities = mutableListOf<GrantedAuthority>()
        account.roles.forEach {
          grantedAuthorities.add(SimpleGrantedAuthority(it))
        }
        return grantedAuthorities
      }
      override fun getUsername() = account.username
      override fun getPassword() = account.password
      override fun isEnabled() = true
      override fun isCredentialsNonExpired() = true
      override fun isAccountNonExpired() = true
      override fun isAccountNonLocked() = true
    }
  }

}