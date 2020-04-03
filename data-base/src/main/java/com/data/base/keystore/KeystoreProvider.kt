package com.data.base.keystore

/**
 * Provides security sensitive keys for the app.
 * Currently it uses SharedPreferences but it is supposed to use Android [java.security.KeyStore]
 * */
interface KeystoreProvider {
    fun store(keyAlias: KeyAlias, value: String)
    fun get(keyAlias: KeyAlias): String
    operator fun invoke(keyAlias: KeyAlias) = get(keyAlias)
}

val KeystoreProvider.username
        get() = invoke(KeyAlias.Username)
val KeystoreProvider.password
        get() = invoke(KeyAlias.Password)
val KeystoreProvider.token
        get() = invoke(KeyAlias.Token)

fun KeystoreProvider.storeCredentials(username: String, password: String, token: String) {
    store(KeyAlias.Username, username)
    store(KeyAlias.Password, password)
    store(KeyAlias.Token, token)
}

sealed class KeyAlias(val name: String) {
    object Username : KeyAlias("username")
    object Password : KeyAlias("password")
    object Token : KeyAlias("token")
}
