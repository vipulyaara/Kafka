package com.kafka.reader

import com.pdftron.pdf.PDFViewCtrl
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object TrialKeyProvider {
    fun generateTrialKey(): String? {
        var urlConnection: HttpURLConnection? = null
        val var6: String
        try {
            val url = URL("https://pws-collect.pdftron.com/api/key")
            urlConnection = url.openConnection() as HttpURLConnection
            val responseCode = urlConnection.responseCode
            if (responseCode != 200) {
                return null
            }
            val `in`: InputStream = BufferedInputStream(urlConnection.inputStream)
            val s = PDFViewCtrl.readStream(`in`)
            var6 = try {
                val keyJson = JSONObject(s)
                keyJson.optString("key")
            } catch (var11: Exception) {
                return null
            }
        } catch (var12: Exception) {
            var12.printStackTrace()
            return null
        } finally {
            urlConnection?.disconnect()
        }
        return var6
    }
}
