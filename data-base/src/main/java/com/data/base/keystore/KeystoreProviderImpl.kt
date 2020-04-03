package com.data.base.keystore

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeystoreProviderImpl @Inject constructor(
    private val keyEncryptionHandler: KeyEncryptionHandler,
    private val keyDecryptionHandler: KeyDecryptionHandler
) {

    private val aliasUsername = "ALIAS_USERNAME"
    private val aliasPassword = "ALIAS_PASSWORD"
    private val aliasToken = "ALIAS_TOKEN"

    fun getUsername() = keyDecryptionHandler.decryptData(
            aliasUsername,
    keyEncryptionHandler.encryption,
    keyEncryptionHandler.iv
    )

    fun getPassword() = keyDecryptionHandler.decryptData(
        aliasPassword,
        keyEncryptionHandler.encryption,
        keyEncryptionHandler.iv
    )

    fun getFormattedToken() = "Bearer ${getToken()}"

    fun getToken() = keyDecryptionHandler.decryptData(
        aliasToken,
        keyEncryptionHandler.encryption,
        keyEncryptionHandler.iv
    )

    fun setUsername(username: String) = keyEncryptionHandler.encryptText(aliasUsername, username)

    fun setPassword(password: String) = keyEncryptionHandler.encryptText(aliasPassword, password)

    fun setToken(token: String) = keyEncryptionHandler.encryptText(aliasToken, token)

    fun setCredentials(username: String, password: String, token: String) {
        setUsername(username)
        setPassword(password)
        setToken(token)
    }

    fun getCredentials() = getUsername() to getPassword()
}
