package org.dailyepisode

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application


fun main(args: Array<String>) {
  val springApplication = SpringApplication(Application::class.java)
  springApplication.setAdditionalProfiles("test")

  runApplication<Application>(*args)
}