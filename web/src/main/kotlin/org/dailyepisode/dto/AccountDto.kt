package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountDto(
  val id: Long?,
  val username: String,
  val email: String,
  val notificationIntervalInDays: Int,
  val isAdmin: Boolean,
  val nrOfSubscriptions: Int
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountRegistrationRequestDto(
  val username: String?,
  val email: String?,
  val password: String?,
  val notificationIntervalInDays: Int?
)

data class AccountUpdateRequestDto(
  val accountId: Long,
  val username: String,
  val notificationIntervalInDays: Int
)

data class PasswordChangeRequestDto(
  val accountId: Long,
  val newPassword: String
)