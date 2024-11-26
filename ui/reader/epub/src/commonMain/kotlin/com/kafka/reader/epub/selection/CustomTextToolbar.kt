package com.kafka.reader.epub.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

class CustomTextToolbar : TextToolbar {
    private val _status = mutableStateOf(TextToolbarStatus.Hidden)
    private val _selectionRect = mutableStateOf<Rect?>(null)
    private val _selectedText = mutableStateOf("")
    private val _onCopyRequested = mutableStateOf<(() -> Unit)?>(null)
    private val _isVisible = mutableStateOf(false)

    override var status: TextToolbarStatus
        get() = _status.value
        set(value) {
            _status.value = value
            _isVisible.value = value == TextToolbarStatus.Shown
        }

    override fun hide() {
        status = TextToolbarStatus.Hidden
        _selectionRect.value = null
        _onCopyRequested.value = null
        _selectedText.value = ""
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        status = TextToolbarStatus.Shown
        _selectionRect.value = rect
        _onCopyRequested.value = onCopyRequested
    }

    @Composable
    fun FloatingToolbar(
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit
    ) {
        val rect = _selectionRect.value
        val density = LocalDensity.current

        if (_isVisible.value && rect != null) {
            var toolbarHeight by remember { mutableStateOf(0) }
            var toolbarWidth by remember { mutableStateOf(0) }

            Popup(
                onDismissRequest = { hide() },
                properties = PopupProperties(
                    focusable = false,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                ),
                offset = with(density) {
                    val gap = 16.dp.toPx()
                    val padding = 8.dp.toPx()
                    
                    // Calculate x position
                    val idealX = rect.center.x - (toolbarWidth / 2f)
                    val minX = padding
                    val maxX = rect.right - toolbarWidth - padding
                    
                    // If the available space is too small, just use minimum padding
                    val x = if (maxX < minX) {
                        minX
                    } else {
                        idealX.coerceIn(minX, maxX)
                    }

                    // Calculate y position
                    val y = (rect.top - toolbarHeight - gap)
                        .coerceAtLeast(padding)

                    IntOffset(
                        x = x.roundToInt(),
                        y = y.roundToInt()
                    )
                }
            ) {
                Surface(
                    modifier = modifier.onGloballyPositioned { coordinates ->
                        toolbarHeight = coordinates.size.height
                        toolbarWidth = coordinates.size.width
                    },
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        content()
                    }
                }
            }
        }
    }
} 