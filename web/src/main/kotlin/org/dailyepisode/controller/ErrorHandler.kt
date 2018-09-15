package org.dailyepisode.controller

import org.dailyepisode.account.*
import org.dailyepisode.series.SeriesNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException

@ControllerAdvice
class ErrorHandler {

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun jsonParseException(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto("json parse error"), HttpStatus.BAD_REQUEST)

  @ExceptionHandler(HttpClientErrorException::class)
  fun connectionException(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto("internal connection error"), HttpStatus.INTERNAL_SERVER_ERROR)

  @ExceptionHandler(ForbiddenAccessException::class)
  fun forbiddenAccess(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.FORBIDDEN)

  @ExceptionHandler(AccountHasNoMatchingSubscriptionException::class)
  fun accountHasNoMatchingSubscription(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(NoAccountFoundException::class)
  fun noAccountFound(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(EmailAlreadyInUseException::class)
  fun emailAlreadyInUse(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

  @ExceptionHandler(InvalidUserNameException::class)
  fun invalidUserName(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.BAD_REQUEST)

  @ExceptionHandler(InvalidEmailException::class)
  fun invalidEmail(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.BAD_REQUEST)

  @ExceptionHandler(InvalidPasswordException::class)
  fun invalidPassword(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.BAD_REQUEST)

  @ExceptionHandler(SeriesNotFoundException::class)
  fun seriesNotFound(exception: Exception): ResponseEntity<ErrorDto> =
    ResponseEntity(ErrorDto(exception.message!!), HttpStatus.CONFLICT)

}

data class ErrorDto(
  val message: String
)