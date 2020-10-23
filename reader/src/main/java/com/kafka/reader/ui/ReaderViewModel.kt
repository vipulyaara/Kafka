package com.kafka.reader.ui

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.extensions.debug
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.kafka.reader.ReaderViewState
import com.kafka.reader.domain.ReadFromUrl
import com.kafka.ui_common.base.ReduxViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.InputStream

class ReaderViewModel @ViewModelInject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val readFromUrl: ReadFromUrl
) : ReduxViewModel<ReaderViewState>(ReaderViewState()) {

    fun preparePdfUrlGdrive(url: String) = "https://docs.google.com/gview?embedded=true&url=$url"

    fun getPdfIntent(url: String?) = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(url?.toUri(), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        debug { "Opening pdf for $url" }
    }

    fun read(context: Context, pdfView: PDFView, readerUrl: String) {
//        val config = ViewerConfig.Builder()
//            .openUrlCachePath(context.cacheDir.absolutePath)
//            .fullscreenModeEnabled(true)
//            .multiTabEnabled(false)
//            .documentEditingEnabled(false)
//            .longPressQuickMenuEnabled(true)
//            .toolbarTitle("")
//            .showSearchView(true)
//            .build()
//        DocumentActivity.openDocument(context, readerUrl.toUri(), config)

        viewModelScope.launch {
            readFromUrl(readerUrl).also {
                it.collectLatest {
                    read(pdfView, it)
                }
            }
        }
    }

    private fun read(pdfView: PDFView, url: InputStream) {
        pdfView.fromStream(url)
            .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            .spacing(0)
            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
            .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
            .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
            .pageSnap(false) // snap pages to screen boundaries
            .pageFling(false) // make a fling change only a single page like ViewPager
            .nightMode(false) // toggle night mode
            .load()
    }

}

