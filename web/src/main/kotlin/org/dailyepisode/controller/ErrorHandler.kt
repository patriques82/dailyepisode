package org.dailyepisode.controller

import org.dailyepisode.dto.ErrorDto
import org.dailyepisode.exception.AccountHasNoMatchingSubscriptionException
import org.dailyepisode.exception.EmailAlreadyInUseException
import org.dailyepisode.exception.InvalidAccountException
import org.dailyepisode.exception.NoAccountFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun jsonParseException(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto("json parse error"), HttpStatus.BAD_REQUEST)

  @ExceptionHandler(AccountHasNoMatchingSubscriptionException::class)
  fun accountHasNoMatchingSubscription(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(NoAccountFoundException::class)
  fun noAccountFound(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(EmailAlreadyInUseException::class)
  fun emailAlreadyInUse(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(InvalidAccountException::class)
  fun invalidAccount(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.BAD_REQUEST)

}