package org.dailyepisode

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

@SpringBootApplication
class Application

class BeanInitializer : ApplicationContextInitializer<GenericApplicationContext> {
  override fun initialize(applicationContext: GenericApplicationContext) {
    val beans = beans {
      //bean<MovieService> { TheMovieDBWebService() }
    }.initialize(applicationContext)
  }
}

fun main(args: Array<String>) {
  SpringApplication(Application::class.java).run(*args)
}