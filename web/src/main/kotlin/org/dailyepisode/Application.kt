package org.dailyepisode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration



@SpringBootApplication(exclude = arrayOf(SecurityAutoConfiguration::class))
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}