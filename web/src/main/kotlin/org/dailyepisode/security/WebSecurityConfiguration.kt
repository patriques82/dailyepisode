package org.dailyepisode.security

import org.dailyepisode.account.Account
import org.dailyepisode.account.AccountStorageService
import org.dailyepisode.account.ToManyLoginAttempts
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextListener
import javax.servlet.annotation.WebListener
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class WebSecurityConfiguration(private val userDetailsService: UserDetailsService,
                               private val authEntryPoint: AuthenticationEntryPoint,
                               private val passwordEncoder: PasswordEncoder
): WebSecurityConfigurerAdapter() {

  // Authentication
  override fun configure(auth: AuthenticationManagerBuilder) {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder)
  }

  // Authorization
  override fun configure(http: HttpSecurity) {
    http
      .cors()
        .and()
      .csrf()
        .disable()
        .headers().frameOptions().sameOrigin() // to enable h2-console
        .and()
      .authorizeRequests()
        .antMatchers("/api/series/**").permitAll()
        .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/resources/**").permitAll()
        .anyRequest().permitAll()
        .and()
      .httpBasic()
        .authenticationEntryPoint(authEntryPoint)
  }

}

@Component
internal class UserDetailsServiceImpl(private val accountStorageService: AccountStorageService,
                                      private val loginAttemptService: LoginAttemptService,
                                      private val request: HttpServletRequest)
  : UserDetailsService {

  override fun loadUserByUsername(username: String?): UserDetails {
    val ip = getClientIP()
    if (loginAttemptService.isBlocked(ip)) {
      throw ToManyLoginAttempts("blocked");
    }
    val account = username?.let { accountStorageService.findByUserName(it) }
    if (account == null) {
      throw UsernameNotFoundException("Username: '$username' does not exists")
    }
    return createUserDetails(account)
  }

  private fun getClientIP(): String {
    val xfHeader = request.getHeader("X-Forwarded-For") ?: return request.remoteAddr
    return xfHeader.split(",".toRegex())[0]
  }

  private fun createUserDetails(account: Account): UserDetails {
    return object : UserDetails {
      override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val grantedAuthorities = mutableListOf<GrantedAuthority>()
        val adminRole = SimpleGrantedAuthority("ROLE_ADMIN")
        val userRole = SimpleGrantedAuthority("ROLE_USER")
        if (account.isAdmin) {
          grantedAuthorities.addAll(listOf(adminRole, userRole))
        } else {
          grantedAuthorities.add(userRole)
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

@Component
internal class AuthenticationEntryPointImpl: AuthenticationEntryPoint {

  override fun commence(request: HttpServletRequest?,
                        response: HttpServletResponse?,
                        authException: AuthenticationException?) {
    response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized succer")
  }

}

@Component
internal class PasswordEncoderImpl: PasswordEncoder by BCryptPasswordEncoder()

@Configuration
@WebListener
internal class RequestContextListenerImpl: RequestContextListener()