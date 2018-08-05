package org.dailyepisode.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.dailyepisode.account.Account

@JsonInclude(JsonInclude.Include.NON_NULL)
class AccountDto(
  val id: Long?,
  val username: String,
  val email: String,
  val password: String
)

fun AccountDto.toAccount(): Account =
  Account(id, username, email, password)

fun Account.toDto(): AccountDto =
  AccountDto(id, username, email, "")


