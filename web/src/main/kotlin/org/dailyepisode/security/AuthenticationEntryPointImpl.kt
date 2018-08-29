package org.dailyepisode.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
internal class AuthenticationEntryPointImpl: AuthenticationEntryPoint {
  override fun commence(request: HttpServletRequest?,
                        response: HttpServletResponse?,
                        authException: AuthenticationException?) {
    response?.status = HttpServletResponse.SC_UNAUTHORIZED
  }
}