package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountDto(
  val id: Long?,
  val username: String,
  val email: String,
  val notificationIntervalInDays: Int
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountRegistrationRequestDto(
  val username: String?,
  val email: String?,
  val password: String?,
  val notificationIntervalInDays: Int?
)