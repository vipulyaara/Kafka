package com.kafka.textreader.bouquet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.net.toUri
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
class HorizontalPdfReaderState(
    resource: ResourceType,
    isZoomEnable: Boolean = false
) : PdfReaderState(resource, isZoomEnable) {

    internal var pagerState: PagerState = PagerState()

    override val currentPage: Int
        get() = pagerState.currentPage

    override val isScrolling: Boolean
        get() = pagerState.isScrollInProgress

    companion object {
        val Saver: Saver<HorizontalPdfReaderState, *> = listSaver(
            save = {
                val resource = it.file?.let { file ->
                    ResourceType.Local(
                        file.toUri()
                    )
                } ?: it.resource
                listOf(
                    resource,
                    it.isZoomEnable,
                    it.pagerState.currentPage
                )
            },
            restore = {
                HorizontalPdfReaderState(
                    it[0] as ResourceType,
                    it[1] as Boolean
                ).apply {
                    pagerState = PagerState(currentPage = it[2] as Int)
                }
            }
        )
    }
}

@Composable
fun rememberHorizontalPdfReaderState(
    resource: ResourceType,
    isZoomEnable: Boolean = true
): HorizontalPdfReaderState {
    return rememberSaveable(saver = HorizontalPdfReaderState.Saver) {
        HorizontalPdfReaderState(resource, isZoomEnable)
    }
}
