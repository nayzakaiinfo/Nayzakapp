package com.example.analytics

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

/**
 * Google Analytics 4 (GA4) Analytics Manager.
 *
 * Implements Google Analytics 4 Measurement Protocol for mobile applications.
 * - Zero login, zero personal data (PII) collected.
 * - Anonymous pseudo-random client_id generated locally per device installation.
 * - Tracks custom events:
 *     1) game_start (category)
 *     2) game_complete (score, correct_count, max_streak, accuracy)
 *     3) share_click (score, player_title)
 *
 * 100% Free with Google Analytics 4.
 */
class GoogleAnalytics4Manager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("ga4_analytics_prefs", Context.MODE_PRIVATE)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // GA4 Measurement ID (format: G-XXXXXXXXXX). Can be configured by the user at runtime.
    @Volatile
    var measurementId: String = prefs.getString(PREF_MEASUREMENT_ID, DEFAULT_MEASUREMENT_ID) ?: DEFAULT_MEASUREMENT_ID
        private set

    // Optional API Secret for GA4 Measurement Protocol
    @Volatile
    var apiSecret: String = prefs.getString(PREF_API_SECRET, "") ?: ""
        private set

    // Completely anonymous UUID. Contains no device hardware ID, no advertising ID, no user data.
    val anonymousClientId: String by lazy {
        var id = prefs.getString(PREF_CLIENT_ID, null)
        if (id.isNullOrBlank()) {
            id = UUID.randomUUID().toString()
            prefs.edit().putString(PREF_CLIENT_ID, id).apply()
        }
        id
    }

    /**
     * Updates the GA4 Measurement ID whenever provided by the user.
     * Example: "G-1A2B3C4D5E"
     */
    fun setMeasurementId(newId: String, secret: String = "") {
        measurementId = newId.trim()
        apiSecret = secret.trim()
        prefs.edit()
            .putString(PREF_MEASUREMENT_ID, measurementId)
            .putString(PREF_API_SECRET, apiSecret)
            .apply()
        Log.i(TAG, "GA4 Measurement ID updated to: $measurementId")
    }

    /**
     * Logs the 'game_start' custom event when a player begins a quiz round.
     */
    fun logGameStart(category: String) {
        val params = mapOf(
            "category" to category,
            "timestamp" to System.currentTimeMillis()
        )
        sendEvent("game_start", params)
    }

    /**
     * Logs the 'game_complete' custom event when the player finishes all 10 questions.
     */
    fun logGameComplete(
        score: Int,
        correctCount: Int,
        maxStreak: Int,
        accuracyPercentage: Int
    ) {
        val params = mapOf(
            "score" to score,
            "correct_count" to correctCount,
            "max_streak" to maxStreak,
            "accuracy" to accuracyPercentage,
            "timestamp" to System.currentTimeMillis()
        )
        sendEvent("game_complete", params)
    }

    /**
     * Logs the 'share_click' custom event when the player clicks the share button to challenge friends.
     */
    fun logShareClick(score: Int, playerTitle: String) {
        val params = mapOf(
            "score" to score,
            "player_title" to playerTitle,
            "timestamp" to System.currentTimeMillis()
        )
        sendEvent("share_click", params)
    }

    /**
     * Dispatches the event payload to GA4 Measurement Protocol asynchronously.
     */
    private fun sendEvent(eventName: String, params: Map<String, Any>) {
        Log.d(TAG, "📊 Logging GA4 Event: [$eventName] with params: $params")

        // If Measurement ID is not yet configured with a valid G- prefix, log locally for verification
        if (!measurementId.startsWith("G-") || measurementId == DEFAULT_MEASUREMENT_ID) {
            Log.i(TAG, "ℹ️ GA4 Measurement ID is pending ('$measurementId'). Event [$eventName] recorded locally.")
            return
        }

        scope.launch {
            try {
                val endpoint = buildString {
                    append("https://www.google-analytics.com/mp/collect?measurement_id=")
                    append(measurementId)
                    if (apiSecret.isNotBlank()) {
                        append("&api_secret=")
                        append(apiSecret)
                    }
                }

                val payload = JSONObject().apply {
                    put("client_id", anonymousClientId)
                    put("non_personalized_ads", true) // Complete privacy compliance
                    val eventsArray = JSONArray().apply {
                        val eventObj = JSONObject().apply {
                            put("name", eventName)
                            val paramsObj = JSONObject()
                            for ((k, v) in params) {
                                paramsObj.put(k, v)
                            }
                            put("params", paramsObj)
                        }
                        put(eventObj)
                    }
                    put("events", eventsArray)
                }

                val url = URL(endpoint)
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 6000
                    readTimeout = 6000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    setRequestProperty("User-Agent", "NayzakAndroid/1.0")
                }

                OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
                    writer.write(payload.toString())
                    writer.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode in 200..299) {
                    Log.d(TAG, "✅ GA4 event [$eventName] successfully delivered to Google Analytics 4 (HTTP $responseCode).")
                } else {
                    Log.w(TAG, "⚠️ GA4 delivery responded with HTTP $responseCode")
                }
                conn.disconnect()
            } catch (e: Exception) {
                Log.w(TAG, "GA4 dispatch skipped (offline or network error): ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "GA4_Analytics"
        const val DEFAULT_MEASUREMENT_ID = "G-PENDING_MEASUREMENT_ID"
        private const val PREF_MEASUREMENT_ID = "pref_ga4_measurement_id"
        private const val PREF_API_SECRET = "pref_ga4_api_secret"
        private const val PREF_CLIENT_ID = "pref_ga4_client_id"

        @Volatile
        private var instance: GoogleAnalytics4Manager? = null

        fun getInstance(context: Context): GoogleAnalytics4Manager {
            return instance ?: synchronized(this) {
                instance ?: GoogleAnalytics4Manager(context).also { instance = it }
            }
        }
    }
}
