package com.kafka.remote.config

expect class RemoteConfig {
    fun get(key: String): String
    fun getBoolean(key: String): Boolean
    fun getLong(key: String): Long
}
