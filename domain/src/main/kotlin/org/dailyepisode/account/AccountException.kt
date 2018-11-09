package org.dailyepisode.account

class AccountHasNoMatchingSubscriptionException(message: String): RuntimeException(message)

class EmailAlreadyInUseException(message: String) : RuntimeException(message)

class NoAccountFoundException(message: String): RuntimeException(message)

class ForbiddenAccessException(message: String): RuntimeException(message)

class IllegalNotificationInterval(message: String): RuntimeException(message)

class ToManyLoginAttempts(message: String): RuntimeException(message)

class NonMatchingAccoundId(message: String): RuntimeException(message)

class InvalidUserNameException(message: String) : RuntimeException(message)

class InvalidPasswordException(message: String) : RuntimeException(message)

class InvalidEmailException(message: String) : RuntimeException(message)
