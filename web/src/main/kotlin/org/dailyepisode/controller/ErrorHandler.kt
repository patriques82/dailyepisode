package org.dailyepisode.controller

import org.dailyepisode.dto.ErrorDto
import org.dailyepisode.exception.AccountHasNoMatchingSubscriptionException
import org.dailyepisode.exception.NoAccountFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.ServletRequest

@ControllerAdvice
class ErrorHandler {

  @ExceptionHandler(AccountHasNoMatchingSubscriptionException::class)
  fun accountHasNoMatchingSubscription(servletRequest: ServletRequest,
                                       exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(NoAccountFoundException::class)
  fun noAccountFound(servletRequest: ServletRequest,
                     exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

}