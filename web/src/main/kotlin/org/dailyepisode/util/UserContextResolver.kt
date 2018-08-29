package org.dailyepisode.util

interface UserContextResolver {
  fun getUser(): String?
  fun getSecurityLevel(): String?
}