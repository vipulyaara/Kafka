package com.kafka.reader.epub.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kafka.common.image.Icons
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.TextHighlight
import kafka.reader.core.parser.EpubCFIParser
import kotlinx.coroutines.delay
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.inverseOnSurfaceDeep
import ui.common.theme.theme.inverseSurfaceDeep
import kotlin.math.roundToInt

//todo: improve text toolbar
class ReaderTextToolbars(
    private val bookId: String,
    private val chapterId: String,
    private val element: ContentElement.Text,
    private val onHighlightCreated: (TextHighlight) -> Unit
) : TextToolbar {
    private val _status = mutableStateOf(TextToolbarStatus.Hidden)
    private val _menuRect = mutableStateOf<Rect?>(null)
    private val _onCopyRequested = mutableStateOf<(() -> Unit)?>(null)
    private val _isMenuVisible = mutableStateOf(false)
    private val _selectedText = mutableStateOf("")
    private var _selectionStart = mutableStateOf(0)
    private var _selectionEnd = mutableStateOf(0)

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

        val selection = TextFieldValue(element.content).selection
        if (!selection.collapsed) {
            val selectedText = element.content.substring(selection.start, selection.end)
            _selectedText.value = selectedText
            _selectionStart.value = selection.start
            _selectionEnd.value = selection.end
        }
    }

    @Composable
    fun MenuContent() {
        val rect = _menuRect.value
        val copyRequested = _onCopyRequested.value
        val density = LocalDensity.current
        val clipboardManager = LocalClipboardManager.current

        if (_isMenuVisible.value && rect != null) {
            // Get the selected text via clipboard
            LaunchedEffect(Unit) {
                copyRequested?.invoke()
                // Small delay to ensure clipboard is updated
                delay(100)
                val selectedText = clipboardManager.getText()?.text ?: ""
                if (selectedText.isNotEmpty()) {
                    val startIndex = element.content.indexOf(selectedText)
                    if (startIndex != -1) {
                        _selectedText.value = selectedText
                        _selectionStart.value = startIndex
                        _selectionEnd.value = startIndex + selectedText.length
                    }
                }
                // Clear clipboard
                clipboardManager.setText(AnnotatedString(""))
            }

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
                MenuContent(
                    copyRequested = copyRequested,
                    selectedText = _selectedText.value
                )
            }
        }
    }

    @Composable
    private fun MenuContent(
        copyRequested: (() -> Unit)?,
        selectedText: String
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = Dimens.Elevation02,
            shadowElevation = Dimens.Elevation02,
            color = MaterialTheme.colorScheme.inverseSurfaceDeep,
            contentColor = MaterialTheme.colorScheme.inverseOnSurfaceDeep
        ) {
            Row(
                modifier = Modifier.padding(horizontal = Dimens.Spacing04),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04)
            ) {
                IconButton(onClick = {
                    if (_selectedText.value.isNotEmpty()) {
                        val highlight = createHighlight(
                            bookId = bookId,
                            chapterId = chapterId,
                            element = element,
                            startOffset = _selectionStart.value,
                            endOffset = _selectionEnd.value
                        )
                        onHighlightCreated(highlight)
                    }
                    hide()
                }) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        HighlightIcon()
                    }
                }

                IconButton(onClick = {
                    copyRequested?.invoke()
                    hide()
                }) {
                    Icon(imageVector = Icons.Copy, contentDescription = "Copy")
                }

                IconButton(onClick = { hide() }) {
                    Icon(imageVector = Icons.Translate, contentDescription = "Translate")
                }
            }
        }
    }

    @Composable
    private fun HighlightIcon(modifier: Modifier = Modifier) {
        Box(
            modifier
                .size(Dimens.Spacing20)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }

    private fun createHighlight(
        bookId: String,
        chapterId: String,
        element: ContentElement.Text,
        startOffset: Int,
        endOffset: Int,
        color: TextHighlight.Color = TextHighlight.Color.BLUE
    ): TextHighlight {
        val cfi = EpubCFIParser.generate(
            chapterId = chapterId,
            elementPath = element.elementPath,
            startOffset = startOffset,
            endOffset = endOffset
        )

        return TextHighlight(
            bookId = bookId,
            chapterId = chapterId,
            text = element.content.substring(startOffset, endOffset),
            cfiRange = cfi,
            color = color.code
        )
    }
}

@Composable
fun ReaderSelectionContainers(
    bookId: String,
    chapterId: String,
    element: ContentElement.Text,
    onHighlightCreated: (TextHighlight) -> Unit,
    content: @Composable () -> Unit
) {
    val toolbar = remember {
        ReaderTextToolbars(
            bookId = bookId,
            chapterId = chapterId,
            element = element,
            onHighlightCreated = onHighlightCreated
        )
    }

    CompositionLocalProvider(LocalTextToolbar provides toolbar) {
        Box(modifier = Modifier.fillMaxSize()) {
            SelectionContainer(Modifier.fillMaxSize()) {
                content()
            }
            toolbar.MenuContent()
        }
    }
}
