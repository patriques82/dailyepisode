package org.dailyepisode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication(exclude = arrayOf(SecurityAutoConfiguration::class))
//@EnableScheduling
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}