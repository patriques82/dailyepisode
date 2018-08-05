package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.account.Account

@JsonInclude(JsonInclude.Include.NON_NULL)
class AccountRegistrationDto(
  val id: Long?,
  val username: String,
  val email: String,
  val password: String
)

fun AccountRegistrationDto.toAccount(): Account =
  Account(id, username, email)

@JsonInclude(JsonInclude.Include.NON_NULL)
class AccountDto(
  val id: Long?,
  val username: String,
  val email: String
)

fun AccountDto.toAccount(): Account =
  Account(id, username, email)

fun Account.toDto(): AccountDto =
  AccountDto(id, username, email)


