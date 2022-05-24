package com.kafka.reader

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.pdftron.pdf.PDFViewCtrl
import org.kafka.reader.R

//import org.rekhta.homepage.R

@SuppressLint("InflateParams")
@Composable
internal fun ReaderView(pdfUrl: String) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(factory = { context ->
        val reader =
            LayoutInflater.from(context).inflate(R.layout.view_reader, null, false) as PDFViewCtrl
//        SetupReader(reader, lifecycleOwner)
        reader
    }) { view ->
//        view.openUrlAsync(pdfUrl, null, null, null)
    }
}

@Composable
fun SetupReader(reader: PDFViewCtrl, lifecycleOwner: LifecycleOwner) {
    AttachLifecycle(reader, lifecycleOwner)
}

//@Composable
//fun readerConfig(): ViewerConfig {
//    val context = LocalContext.current
//    val config = readerViewConfig()
//    return remember {
//        ViewerConfig.Builder().useStandardLibrary(true).pdfViewCtrlConfig(config).build()
//    }
//}
//
//@Composable
//fun readerViewConfig(): PDFViewCtrlConfig {
//    val context = LocalContext.current
//    return remember {
//        PDFViewCtrlConfig.getDefaultConfig(context).setClientBackgroundColor(R.color.yellow)
//            .setClientBackgroundColorDark(R.color.dark_gray).setHighlightFields(true)
//            .setImageSmoothing(true).setUrlExtraction(true).setMaintainZoomEnabled(true)
//    }
//}

@Composable
fun AttachLifecycle(reader: PDFViewCtrl, lifecycleOwner: LifecycleOwner) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    reader.pause()
                    reader.purgeMemory()
                }
                Lifecycle.Event.ON_STOP -> {
                    reader.resume()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    reader.destroy()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
