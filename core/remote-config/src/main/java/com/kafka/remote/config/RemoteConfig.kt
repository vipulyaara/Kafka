package com.kafka.remote.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.kafka.base.debug
import org.kafka.base.errorLog
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val REMOTE_CONFIG_FETCH_INTERVAL_SECONDS = 3600L

@Singleton
class RemoteConfig @Inject constructor(private val json: Json) {

    private val remoteConfig by lazy {
        Firebase.remoteConfig.apply {
            val config = remoteConfigSettings {
                minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL_SECONDS
            }
            setConfigSettingsAsync(config)
        }
    }

    init {
        try {
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                debug { "Fetch and activate remote config completed" }
            }
        } catch (e: Exception) {
            errorLog(e) { "Error fetching remote config" }
        }
    }

    fun get(key: String): String = remoteConfig.getString(key)

    private fun optional(key: String): String? = get(key).let { it.ifBlank { null } }

    private fun <T> optional(key: String, serializer: KSerializer<T>): T? {
        return optional(key).let {
            try {
                json.decodeFromString(serializer, it.orEmpty())
            } catch (e: SerializationException) {
                Timber.e(e)
                errorLog(e) { "Error parsing remote config" }
                null
            }
        }
    }

    fun <T> get(name: String, serializer: KSerializer<T>, defaultValue: T): T {
        return optional(name, serializer).let {
            when (it) {
                null -> defaultValue
                else -> it
            }
        }
    }
}
