package com.kafka.reader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity

class ReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        read(intent.getStringExtra("url") ?: "")
    }

    private fun read(readerUrl: String) {
        val config = ViewerConfig.Builder()
            .openUrlCachePath(cacheDir.absolutePath)
            .fullscreenModeEnabled(true)
            .multiTabEnabled(false)
            .documentEditingEnabled(false)
            .longPressQuickMenuEnabled(false)
            .toolbarTitle("")
            .showSearchView(false)
            .build()
        DocumentActivity.openDocument(this, readerUrl.toUri(), config)
    }
}
