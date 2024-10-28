package com.kafka.networking

import com.kafka.base.ApplicationScope
import com.kafka.base.debug
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import me.tatarka.inject.annotations.Provides
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

interface NetworkingComponent {

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    @Provides
    @ApplicationScope
    fun provideSerializersModule(
        polymorphicDefaultPairs: Set<SerializationPolymorphicDefaultPair<*>>,
    ): SerializersModule = SerializersModule {
        polymorphicDefaultPairs.forEach { (base, default) ->
            polymorphicDefaultDeserializer(base as KClass<Any>) { default.serializer() }
        }
    }

    @Provides
    @ApplicationScope
    fun jsonConfigured(serializersModule: SerializersModule) = Json {
        ignoreUnknownKeys = true
        useAlternativeNames = false
        isLenient = true
        prettyPrint = true
        encodeDefaults = true
        this.serializersModule = serializersModule
    }

    @ApplicationScope
    @Provides
    fun provideHttpClient(json: Json): HttpClient = HttpClient {
//        install(ContentNegotiation) {
//            json(json)
//        }

        install(HttpTimeout) {
            requestTimeoutMillis = Config.API_TIMEOUT
            connectTimeoutMillis = Config.API_TIMEOUT
            socketTimeoutMillis = Config.API_TIMEOUT
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    debug { message }
                }

            }
            level = LogLevel.ALL
        }

        defaultRequest {
            accept(ContentType.parse("application/json"))
            header("content-type", "application/json")
        }
    }

    object Config {
        val API_TIMEOUT = 40.seconds.inWholeMilliseconds
    }
}