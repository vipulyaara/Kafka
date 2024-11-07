package com.kafka.common.adaptive

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable

fun LazyGridScope.fullSpanItem(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit,
) {
    item(
        key = key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = contentType,
        content = content,
    )
}

fun <T> LazyGridScope.fullSpanItems(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    items(
        items = items,
        key = key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = contentType,
        itemContent = itemContent,
    )
}


fun LazyGridScope.fullSpanItems(
    count: Int,
    key: ((inex: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit
) {
    items(
        count = count,
        key = key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = contentType,
        itemContent = itemContent,
    )
}
