package org.dailyepisode.security

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomWebSecurityConfigurationAdapter(val userDetailsService: UserDetailsService,
                                            val passwordEncoder: PasswordEncoder
): WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.csrf().disable()
    http.headers().frameOptions().sameOrigin() // to enable h2-console ???
    http.authorizeRequests()
      .antMatchers("/api/*").authenticated()
      .anyRequest().permitAll()
      .and()
      .formLogin().permitAll()
  }

  override fun configure(auth: AuthenticationManagerBuilder) {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder)
  }

}