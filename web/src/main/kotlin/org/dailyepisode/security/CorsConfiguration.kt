package org.dailyepisode.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
internal class CorsConfiguration(@Value("\${cors.allowed.origin}") private val allowedOrigin: String)
  : WebMvcConfigurer {

  override fun addCorsMappings(registry: CorsRegistry) {
    registry
      .addMapping("/**")
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowedOrigins(allowedOrigin)
  }

}