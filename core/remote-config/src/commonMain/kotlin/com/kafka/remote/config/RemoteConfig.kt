package com.kafka.remote.config

import com.kafka.base.ApplicationScope
import com.kafka.base.ProcessLifetime
import com.kafka.base.errorLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

const val REMOTE_CONFIG_FETCH_INTERVAL_SECONDS = 3600L

@ApplicationScope
@Inject
class RemoteConfig(
    private val json: Json,
    @ProcessLifetime private val scope: CoroutineScope
) {
//    private val remoteConfig by lazy { Firebase.remoteConfig }

    fun init() {
//        scope.launch {
//            try {
//                remoteConfig.fetchAndActivate()
//                remoteConfig.setDefaults(*getDefaults())
//                remoteConfig.settings {
//                    minimumFetchInterval = REMOTE_CONFIG_FETCH_INTERVAL_SECONDS.seconds
//                }
//            } catch (e: Exception) {
//                errorLog(e) { "Error fetching remote config" }
//            }
//        }
    }

    fun get(key: String): String = "remoteConfig[key]"

    fun getBoolean(key: String): Boolean = true

    fun getLong(key: String): Long = 0L

    private fun getDefaults(): Array<Pair<String, Any?>> {
        return arrayOf(
            IS_SHARE_ENABLED to true,
            ITEM_DETAIL_DYNAMIC_THEME_ENABLED to true,
            TRUE_CONTRAST_ENABLED to true
        )
    }

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
