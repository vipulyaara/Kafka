package org.kafka.domain.interactors

import android.app.Application
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class ReadTextFromUri @Inject constructor(private val application: Application) {
    suspend operator fun invoke(uri: Uri): Result<List<String>> {
        return try {
            val pages = mutableListOf<String>()
            val `in`: InputStream? = application.contentResolver.openInputStream(uri)
            val r = BufferedReader(InputStreamReader(`in`))
            val page = StringBuilder()
            var lineNumber = 1
            var line: String?

            while (withContext(Dispatchers.IO) {
                    r.readLine()
                }.also { line = it } != null
            ) {
                if (lineNumber % 30 == 0) {
                    pages.add(page.toString())
                    page.clear()
                }
                page.append(line).append('\n')
                lineNumber++
            }
            pages.add(page.toString())
            Result.success(pages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
