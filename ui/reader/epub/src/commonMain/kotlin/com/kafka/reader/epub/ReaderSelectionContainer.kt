package com.kafka.reader.epub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ui.common.theme.theme.Dimens
import kotlin.math.roundToInt

//todo: improve text toolbar
class ReaderTextToolbar : TextToolbar {
    private val _status = mutableStateOf(TextToolbarStatus.Hidden)
    private val _menuRect = mutableStateOf<Rect?>(null)
    private val _onCopyRequested = mutableStateOf<(() -> Unit)?>(null)
    private val _isMenuVisible = mutableStateOf(false)

    override var status: TextToolbarStatus
        get() = _status.value
        set(value) {
            _status.value = value
        }

    override fun hide() {
        status = TextToolbarStatus.Hidden
        _isMenuVisible.value = false
        _menuRect.value = null
        _onCopyRequested.value = null
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        status = TextToolbarStatus.Shown
        _menuRect.value = rect
        _onCopyRequested.value = onCopyRequested
        _isMenuVisible.value = true
    }

    @Composable
    fun MenuContent() {
        val rect = _menuRect.value
        val copyRequested = _onCopyRequested.value
        val density = LocalDensity.current

        if (_isMenuVisible.value && rect != null) {
            Popup(
                onDismissRequest = { hide() },
                properties = PopupProperties(
                    focusable = false,
                    dismissOnClickOutside = true
                ),
                offset = with(density) {
                    IntOffset(
                        x = (rect.center.x - 100.dp.toPx()).roundToInt(),
                        y = (rect.top - 48.dp.toPx()).roundToInt()
                    )
                }
            ) {
                MenuContent(copyRequested)
            }
        }
    }

    @Composable
    private fun MenuContent(copyRequested: (() -> Unit)?) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = Dimens.Elevation02,
            shadowElevation = Dimens.Elevation02
        ) {
            Row(
                modifier = Modifier.padding(horizontal = Dimens.Spacing04),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04)
            ) {
                TextButton(onClick = {
                    copyRequested?.invoke()
                    hide()
                }) {
                    Text(
                        text = "Copy",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                TextButton(onClick = { hide() }) {
                    Text(
                        text = "Translate",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ReaderSelectionContainer(content: @Composable () -> Unit) {
    val toolbar = remember { ReaderTextToolbar() }

    CompositionLocalProvider(LocalTextToolbar provides toolbar) {
        Box(modifier = Modifier.fillMaxSize()) {
            SelectionContainer(Modifier.fillMaxSize()) {
                content()
            }
            toolbar.MenuContent()
        }
    }
}
