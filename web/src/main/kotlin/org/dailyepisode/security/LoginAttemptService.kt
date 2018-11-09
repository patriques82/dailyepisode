package org.dailyepisode.security

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Service
class LoginAttemptService {

  private val MAX_ATTEMPT = 10
  private val attemptsCache: LoadingCache<String, Int>

  init {
    attemptsCache = CacheBuilder.newBuilder()
      .expireAfterWrite(1, TimeUnit.DAYS)
      .build(object : CacheLoader<String, Int>() {
        override fun load(key: String): Int {
          return 0
        }
      })
  }

  fun loginSuccess(key: String) {
    attemptsCache.invalidate(key)
  }

  fun loginFailed(key: String) {
    var attempts: Int
    try {
      attempts = attemptsCache.get(key).toInt()
    } catch (e: ExecutionException) {
      attempts = 0
    }
    attempts++
    attemptsCache.put(key, attempts)
  }

  fun isBlocked(key: String): Boolean {
    try {
      return attemptsCache.get(key) >= MAX_ATTEMPT
    } catch (e: ExecutionException) {
      return false
    }
  }

}