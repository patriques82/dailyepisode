package org.dailyepisode.security

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class CustomWebSecurityConfigurationAdapter(private val userDetailsService: UserDetailsService,
                                            private val authEntryPoint: AuthenticationEntryPoint,
                                            private val passwordEncoder: PasswordEncoder
): WebSecurityConfigurerAdapter() {

  // Authorization
  override fun configure(http: HttpSecurity) {
    http
      .csrf()
        .disable()
        .headers().frameOptions().sameOrigin() // to enable h2-console
        .and()
      .authorizeRequests()
        .antMatchers("/api/search").permitAll()
        .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/resources/**").permitAll()
        .anyRequest().authenticated()
        .and()
      .httpBasic()
        .authenticationEntryPoint(authEntryPoint)
  }

  // Authentication
  override fun configure(auth: AuthenticationManagerBuilder) {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder)
  }

}