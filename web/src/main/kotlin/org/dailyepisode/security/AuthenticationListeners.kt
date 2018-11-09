package org.dailyepisode.security

import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureListener(val loginAttemptService: LoginAttemptService):
  ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  override fun onApplicationEvent(event: AuthenticationFailureBadCredentialsEvent) {
    val authDetails= event.authentication.details as WebAuthenticationDetails
    loginAttemptService.loginFailed(authDetails.getRemoteAddress());
  }

}

@Component
class AuthenticationSuccessListener(val loginAttemptService: LoginAttemptService):
  ApplicationListener<AuthenticationSuccessEvent> {

  override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
    val authDetails= event.authentication.details as WebAuthenticationDetails
    loginAttemptService.loginSuccess(authDetails.getRemoteAddress());
  }

}