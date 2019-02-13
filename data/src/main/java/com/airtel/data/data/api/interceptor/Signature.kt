package com.airtel.data.data.api.interceptor

import android.util.Base64
import android.util.Log
import java.security.Key
import java.security.SignatureException
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by AkashGupta on 06/04/18.
 *
 */
internal object Signature {
    private const val HMAC_SHA1_ALGORITHM = "HmacSHA1"
    private const val ALGORITHM_256 = "AES/ECB/PKCS7Padding"

    @Throws(SignatureException::class)
    fun calculateRFC2104HMAC(data: String, key: String): String {
        val result: String
        try {
            // get an hmac_sha1 key from the raw key bytes
            val signingKey = SecretKeySpec(key.toByteArray(),
                HMAC_SHA1_ALGORITHM
            )

            // get an hmac_sha1 Mac instance and initialize with the signing key
            val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
            mac.init(signingKey)

            // compute the hmac on input data bytes
            val rawHmac = mac.doFinal(data.toByteArray())

            // base64-encode the hmac
            //result = Base64.encode(rawHmac); // from archiveService
            // base64-encode the hmac
            //result = Base64.encode(rawHmac); // from archiveService
            // "changed Base64 encoding from Base64.DEFAULT to Base64.NO_WRAP to ommit new line characters because it is not allowed in okhttp header"
            //[analytics-0.0.2 53c1a4f] changed Base64 encoding from Base64.DEFAULT to Base64.NO_WRAP
            // to ommit new line characters because it is not allowed in okhttp header
            result = Base64.encodeToString(rawHmac, Base64.NO_WRAP) // Changed for Android
            //old
//            result = Base64.encodeToString(rawHmac, Base64.DEFAULT) // Changed for Android
            //result = Encoding.EncodeBase64(rawHmac);
        } catch (e: Exception) {
            throw SignatureException("Failed to generate HMAC : " + e.message)
        }
        return result
    }

    fun decrypt(encryptedValue: String, token: String): String? {
        return try {
            val key = generateKey(token)
            val c = Cipher.getInstance(ALGORITHM_256, "BC")
            c.init(Cipher.DECRYPT_MODE, key)
            val decodedValue = Base64.decode(encryptedValue.toByteArray(), 0)
            val decryptedVal = c.doFinal(decodedValue)
            String(decryptedVal)
        } catch (e: Exception) {
            Log.d("Exception  : ", e.toString() + "")
            null
        }
    }

    @Throws(Exception::class)
    private fun generateKey(password: String): Key {
        val keyValue = password.toByteArray(charset("UTF8"))
        return SecretKeySpec(keyValue, ALGORITHM_256)
    }
}
