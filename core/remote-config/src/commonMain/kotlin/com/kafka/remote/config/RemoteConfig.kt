package com.kafka.remote.config

import com.chrynan.inject.Inject
import com.chrynan.inject.Singleton
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.kafka.base.ProcessLifetime
import org.kafka.base.errorLog

const val REMOTE_CONFIG_FETCH_INTERVAL_SECONDS = 3600L

@Singleton
class RemoteConfig @Inject constructor(
    private val json: Json,
    @ProcessLifetime private val processScope: CoroutineScope,
) {

    private val remoteConfig by lazy { Firebase.remoteConfig }

    init {
        try {
            processScope.launch {
                remoteConfig.settings { minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL_SECONDS }
                remoteConfig.fetchAndActivate()
            }
        } catch (e: Exception) {
            errorLog(e) { "Error fetching remote config" }
        }
    }

    fun get(key: String): String = remoteConfig[key]

    fun getBoolean(key: String): Boolean = remoteConfig[key]

    fun getLong(key: String): Long = remoteConfig[key]

    private fun optional(key: String): String? = get(key).let { it.ifBlank { null } }

    private fun <T> optional(key: String, serializer: KSerializer<T>): T? {
        return optional(key).let {
            try {
                json.decodeFromString(serializer, it.orEmpty())
            } catch (e: SerializationException) {
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
