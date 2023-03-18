package com.kafka.textreader.bouquet

import android.content.Context
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.net.URL

@Composable
fun VerticalPdfReader(
    state: VerticalPdfReaderState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        val ctx = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val density = LocalDensity.current
        val lazyState = state.lazyState

        DisposableEffect(key1 = Unit) {
            val job = coroutineScope.launch(Dispatchers.IO) {
                load(
                    coroutineScope = coroutineScope,
                    context = ctx,
                    state = state,
                    width = constraints.maxWidth,
                    height = constraints.maxHeight,
                    portrait = true
                )
            }
            onDispose {
                job.cancel()
                state.close()
            }
        }

        state.pdfRender?.let { pdf ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyState
            ) {
                items(pdf.pageCount) {
                    val bitmapState = pdf.pageLists[it].stateFlow.collectAsState()
                    DisposableEffect(key1 = Unit) {
                        pdf.pageLists[it].load()
                        onDispose {
                            pdf.pageLists[it].recycle()
                        }
                    }
                    val height = bitmapState.value.height * state.scale
                    val width = constraints.maxWidth * state.scale
                        PdfImage(
                            graphicsLayerData = {
                                GraphicsLayerData(
                                    scale = state.scale,
                                    translationX = state.offset.x,
                                    translationY = state.offset.y
                                )
                            },
                            bitmap = {
                                bitmapState.value.asImageBitmap()
                            },
                            dimension = {
                                Dimension(
                                    height = with(density) { height.toDp() },
                                    width = with(density) { width.toDp() }
                                )
                            }
                        )
                }
            }
        }
    }
}

private suspend fun load(
    coroutineScope: CoroutineScope,
    context: Context,
    state: PdfReaderState,
    width: Int,
    height: Int,
    portrait: Boolean
) {
    runCatching {
        if (state.isLoaded) {
            val pFD =
                ParcelFileDescriptor.open(state.mFile, ParcelFileDescriptor.MODE_READ_ONLY)
            state.pdfRender = BouquetPdfRender(pFD, width, height, portrait)
        } else {
            when (val res = state.resource) {
                is ResourceType.Local -> {
                    context.contentResolver.openFileDescriptor(res.uri, "r")?.let {
                        state.pdfRender = BouquetPdfRender(it, width, height, portrait)
                    } ?: throw IOException("File not found")
                }

                is ResourceType.Remote -> {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            runCatching {
                                val bufferSize = 8192
                                val url = URL(res.url)
                                val connection = url.openConnection().also { it.connect() }
                                val totalLength = connection.contentLength
                                var downloaded = 0
                                val file = File(context.cacheDir, generateFileName())
                                BufferedInputStream(url.openStream(), bufferSize).use { input ->
                                    file.outputStream().use { output ->
                                        var data = ByteArray(bufferSize)
                                        var count = input.read(data)
                                        while (count != -1) {
                                            if (totalLength > 0) {
                                                downloaded += bufferSize
                                                state.mLoadPercent =
                                                    (downloaded * (100 / totalLength.toFloat())).toInt()
                                            }
                                            output.write(data, 0, count)
                                            data = ByteArray(bufferSize)
                                            count = input.read(data)
                                        }
                                    }
                                }
                                val pFD = ParcelFileDescriptor.open(
                                    file,
                                    ParcelFileDescriptor.MODE_READ_ONLY
                                )
                                state.pdfRender = BouquetPdfRender(pFD, width, height, portrait)
                                state.mFile = file
                            }.onFailure {
                                state.mError = it
                            }
                        }
                    }
                }

                is ResourceType.Base64 -> {
                    coroutineScope.launch {
                        runCatching {
                            val file = context.base64ToPdf(res.file)
                            val pFD = ParcelFileDescriptor.open(
                                file,
                                ParcelFileDescriptor.MODE_READ_ONLY
                            )
                            state.pdfRender = BouquetPdfRender(pFD, width, height, portrait)
                            state.mFile = file
                        }.onFailure {
                            state.mError = it
                        }
                    }
                }
            }
        }
    }.onFailure {
        state.mError = it
    }
}
