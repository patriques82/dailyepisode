package org.dailyepisode.account

class AccountHasNoMatchingSubscriptionException(message: String): RuntimeException(message)

class EmailAlreadyInUseException(message: String) : RuntimeException(message)

class InvalidAccountException(message: String) : RuntimeException(message)

class NoAccountFoundException(message: String): RuntimeException(message)