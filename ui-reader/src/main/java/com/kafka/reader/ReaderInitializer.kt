package com.kafka.reader

import android.content.Context
import com.pdftron.common.PDFNetException
import com.pdftron.pdf.PDFNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kafka.reader.R
import javax.inject.Inject

class ReaderInitializer @Inject constructor() {
    suspend fun initialize(context: Context) {
        val key = withContext(Dispatchers.IO) { TrialKeyProvider.generateTrialKey() }
        if (key != null) {
            try {
                PDFNet.initialize(context, R.raw.pdfnet, key)
            } catch (var4: PDFNetException) {
                var4.printStackTrace()
            }
        }
    }
}
