package org.dailyepisode.controller.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.mail.SimpleMailMessage

@RestController
@RequestMapping("/api/mail")
class NotificationController(val emailSender: JavaMailSender) {

  @PostMapping("/test")
  fun testEmail(): ResponseEntity<String> {
    val message = SimpleMailMessage()
    message.setTo("patrik.nygren@evolvetechnology.se")
    message.setSubject("test")
    message.setText("test")
    emailSender.send(message)
    return ResponseEntity("message sent", HttpStatus.OK)
  }

}