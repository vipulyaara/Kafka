package com.kafka.textreader.bouquet

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.core.net.toUri

class VerticalPdfReaderState(
    resource: ResourceType,
    isZoomEnable: Boolean = false,
    startPage: Int = 0
) : PdfReaderState(resource, isZoomEnable) {

    var lazyState: LazyListState = LazyListState(startPage)
        private set

    override val currentPage: Int
        get() = currentPage()

    override val isScrolling: Boolean
        get() = lazyState.isScrollInProgress

    private fun currentPage(): Int {
        return pdfRender?.let { pdfRender ->
            val currentMinIndex = lazyState.firstVisibleItemIndex
            var lastVisibleIndex = currentMinIndex
            var totalVisiblePortion =
                (pdfRender.pageLists[currentMinIndex].dimension.height * scale) - lazyState.firstVisibleItemScrollOffset
            for (i in currentMinIndex + 1 until pdfPageCount) {
                val newTotalVisiblePortion =
                    totalVisiblePortion + (pdfRender.pageLists[i].dimension.height * scale)
                if (newTotalVisiblePortion <= pdfRender.height) {
                    lastVisibleIndex = i
                    totalVisiblePortion = newTotalVisiblePortion
                } else {
                    break
                }
            }
            lastVisibleIndex + 1
        } ?: 0
    }

    companion object {
        val Saver: Saver<VerticalPdfReaderState, *> = listSaver(
            save = {
                val resource = it.file?.let { file ->
                    ResourceType.Local(
                        file.toUri()
                    )
                } ?: it.resource
                listOf(
                    resource,
                    it.isZoomEnable,
                    it.lazyState.firstVisibleItemIndex,
                    it.lazyState.firstVisibleItemScrollOffset
                )
            },
            restore = {
                VerticalPdfReaderState(
                    it[0] as ResourceType,
                    it[1] as Boolean
                ).apply {
                    lazyState = LazyListState(
                        firstVisibleItemIndex = it[2] as Int,
                        firstVisibleItemScrollOffset = it[3] as Int
                    )
                }
            }
        )
    }
}

@Composable
fun rememberVerticalPdfReaderState(
    resource: ResourceType,
    startPage: Int,
    isZoomEnable: Boolean = true
): VerticalPdfReaderState {
    return remember {
        VerticalPdfReaderState(resource, isZoomEnable, startPage)
    }
}
