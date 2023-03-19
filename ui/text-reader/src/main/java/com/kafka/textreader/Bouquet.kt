package com.kafka.textreader

import android.content.Context
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import java.io.IOException

@Composable
fun VerticalPDFReader(
    state: VerticalPdfReaderState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        val ctx = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val density = LocalDensity.current
        val lazyState = state.lazyState
        DisposableEffect(key1 = Unit) {
            val job = coroutineScope.launch {
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
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures(true) { centroid, pan, zoom, rotation ->
                            if (!state.mIsZoomEnable) return@detectTransformGestures
                            val nScale = (state.scale * zoom)
                                .coerceAtLeast(1f)
                                .coerceAtMost(3f)
                            val nOffset = if (nScale > 1f) {
                                val maxT =
                                    (constraints.maxWidth * state.scale) - constraints.maxWidth
                                Offset(
                                    x = (state.offset.x + pan.x).coerceIn(
                                        minimumValue = -maxT / 2,
                                        maximumValue = maxT / 2
                                    ),
                                    y = 0f
                                )
                            } else {
                                Offset(0f, 0f)
                            }
                            val scaleDiff = nScale - state.scale
                            val oldScale = state.scale
                            val scroll = lazyState.firstVisibleItemScrollOffset / oldScale
                            state.mScale = nScale
                            state.offset = nOffset
                            coroutineScope.launch {
                                lazyState.scrollBy((centroid.y + scroll / 2) * scaleDiff)
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = contentPadding,
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
                    coroutineScope.launch(Dispatchers.IO) {
                        context.contentResolver.openFileDescriptor(res.uri, "r")?.let {
                            state.pdfRender = BouquetPdfRender(it, width, height, portrait)
                            state.mFile = context.uriToFile(res.uri)
                        } ?: run {
                            state.mError = IOException("File not found")
                        }
                    }
                }
            }
        }
    }.onFailure {
        state.mError = it
    }
}
