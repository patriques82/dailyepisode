package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.account.Account

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountRegistrationDto(
  val username: String?,
  val email: String?,
  val password: String?
)

fun AccountRegistrationDto.toAccount(): Account =
  Account(null, username ?: "", email ?: "", password, roles = emptyList<String>())

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountLoginDto(
  val username: String?,
  val password: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountDto(
  val id: Long?,
  val username: String,
  val email: String
)

fun AccountDto.toAccount(): Account =
  Account(id, username, email, password = null, roles = emptyList<String>())

fun Account.toDto(): AccountDto =
  AccountDto(id, username, email)

