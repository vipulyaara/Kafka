package com.kafka.data.keystore

import org.rekhta.base.errorLog
import java.io.IOException
import java.nio.charset.Charset
import java.security.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class KeyDecryptionHandler @Inject constructor() {

    private var keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)

    init {
        try {
            keyStore.load(null)
        } catch (ex: Exception) {
            errorLog(ex) { ex.localizedMessage ?: "" }
        }
    }

    @Throws(
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IOException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class
    )
    fun decryptData(
        alias: String,
        encryptedData: ByteArray?,
        encryptionIv: ByteArray?
    ): String {
        val cipher =
            Cipher.getInstance(TRANSFORMATION)
        val spec =
            GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        return String(cipher.doFinal(encryptedData), Charset.defaultCharset())
    }

    @Throws(
        NoSuchAlgorithmException::class,
        UnrecoverableEntryException::class,
        KeyStoreException::class
    )
    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore!!.getEntry(
            alias,
            null
        ) as KeyStore.SecretKeyEntry).secretKey
    }

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    }
}
