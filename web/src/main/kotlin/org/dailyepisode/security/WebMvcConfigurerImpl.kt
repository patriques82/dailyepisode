package org.dailyepisode.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
internal class WebMvcConfigurerImpl(@Value("\${cors.allowed.origin}") val allowedOrigin: String):
  WebMvcConfigurer {
  override fun addCorsMappings(registry: CorsRegistry) {
    registry
      .addMapping("/api/*")
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowedHeaders("*")
      .allowedOrigins(allowedOrigin)
  }
}