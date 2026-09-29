package com.example.data.engine

import kotlinx.coroutines.delay
import okhttp3.Response
import org.json.JSONObject
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

/** What went wrong with a Gemini call, in terms the learner can act on. */
enum class GeminiErrorKind {
  /** 503/UNAVAILABLE or 529: Google's servers are overloaded. Not the app's fault; retrying helps. */
  OVERLOADED,
  /** 429 per-minute limit of the learner's key. Waiting a bit helps. */
  RATE_LIMITED,
  /** 429 per-day quota of the learner's key. Only tomorrow (or another key) helps. */
  DAILY_QUOTA,
  /** 400 API_KEY_INVALID, 401, 403. */
  INVALID_KEY,
  /** 404: the model name was retired. Needs an app update. */
  MODEL_UNAVAILABLE,
  /** Other 5xx. */
  SERVER,
  TIMEOUT,
  NETWORK,
  /** The prompt or answer was blocked by Gemini's safety filters. */
  BLOCKED,
  /** 200 but no usable text or audio. */
  EMPTY,
  OTHER
}

class GeminiException(
  val kind: GeminiErrorKind,
  val httpCode: Int? = null,
  /** Google's own message, kept for logs and the unexpected cases. */
  val detail: String? = null,
  /** Server-suggested wait before retrying (Retry-After header or RetryInfo), if any. */
  val retryAfterMs: Long? = null,
  cause: Throwable? = null
) : Exception(detail ?: kind.name, cause) {

  /** Whether trying the same request again later has a real chance to succeed. */
  val isRetryable: Boolean
    get() = kind in setOf(GeminiErrorKind.OVERLOADED, GeminiErrorKind.RATE_LIMITED, GeminiErrorKind.SERVER, GeminiErrorKind.TIMEOUT)
}

object GeminiHttp {

  /** Turns a non-2xx response into a [GeminiException]. Reads (and consumes) the body. */
  fun errorFrom(response: Response): GeminiException {
    val raw = runCatching { response.body?.string().orEmpty() }.getOrDefault("")
    val error = runCatching { JSONObject(raw).optJSONObject("error") }.getOrNull()
    val message = error?.optString("message")?.takeIf { it.isNotBlank() } ?: raw.take(200).ifBlank { null }
    val status = error?.optString("status").orEmpty()
    val headerRetry = response.header("Retry-After")?.toLongOrNull()?.times(1000)
    return errorFor(response.code, status, message, retryAfterMs = headerRetry ?: retryInfoMs(error))
  }

  /** Classifies an error object found inside a streamed chunk. */
  fun errorFrom(error: JSONObject): GeminiException =
    errorFor(error.optInt("code"), error.optString("status"), error.optString("message"), retryInfoMs(error))

  private fun errorFor(code: Int, status: String, message: String?, retryAfterMs: Long?): GeminiException {
    val kind = when {
      code == 503 || code == 529 || status == "UNAVAILABLE" -> GeminiErrorKind.OVERLOADED
      code == 429 || status == "RESOURCE_EXHAUSTED" ->
        if (message?.contains("PerDay", ignoreCase = true) == true || message?.contains("per day", ignoreCase = true) == true) {
          GeminiErrorKind.DAILY_QUOTA
        } else {
          GeminiErrorKind.RATE_LIMITED
        }
      code == 401 || code == 403 || message?.contains("API key", ignoreCase = true) == true -> GeminiErrorKind.INVALID_KEY
      code == 404 -> GeminiErrorKind.MODEL_UNAVAILABLE
      code == 504 || status == "DEADLINE_EXCEEDED" -> GeminiErrorKind.TIMEOUT
      code >= 500 -> GeminiErrorKind.SERVER
      else -> GeminiErrorKind.OTHER
    }
    return GeminiException(kind, code.takeIf { it > 0 }, message, retryAfterMs)
  }

  /** Reads google.rpc.RetryInfo ("retryDelay": "37s") from an error's details, if present. */
  private fun retryInfoMs(error: JSONObject?): Long? {
    val details = error?.optJSONArray("details") ?: return null
    for (i in 0 until details.length()) {
      val delay = details.optJSONObject(i)?.optString("retryDelay").orEmpty()
      val seconds = delay.removeSuffix("s").toDoubleOrNull() ?: continue
      return (seconds * 1000).toLong()
    }
    return null
  }

  /** Maps any failure (network, timeout, API) to a [GeminiException]. */
  fun classify(t: Throwable): GeminiException = when (t) {
    is GeminiException -> t
    is SocketTimeoutException -> GeminiException(GeminiErrorKind.TIMEOUT, cause = t)
    is UnknownHostException, is ConnectException -> GeminiException(GeminiErrorKind.NETWORK, cause = t)
    is InterruptedIOException -> GeminiException(GeminiErrorKind.TIMEOUT, cause = t)
    else -> GeminiException(GeminiErrorKind.OTHER, detail = t.message ?: t::class.simpleName, cause = t)
  }

  /**
   * Runs [block] up to [maxAttempts] times while it fails with a retryable error, waiting with
   * exponential backoff and jitter (or the server's own Retry-After). [onRetry] is told before
   * each new attempt so the UI can say "retrying (2/3)".
   */
  suspend fun <T> withRetry(
    maxAttempts: Int = 3,
    baseDelayMs: Long = 2_000,
    onRetry: (attempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
    block: suspend (attempt: Int) -> T
  ): T {
    var attempt = 1
    while (true) {
      try {
        return block(attempt)
      } catch (t: Throwable) {
        if (t is kotlinx.coroutines.CancellationException) throw t
        val error = classify(t)
        if (!error.isRetryable || attempt >= maxAttempts) throw error
        val backoff = baseDelayMs * (1L shl (attempt - 1)) + Random.nextLong(0, 750)
        val wait = (error.retryAfterMs ?: backoff).coerceIn(1_000, 60_000)
        attempt++
        onRetry(attempt, maxAttempts)
        delay(wait)
      }
    }
  }
}
