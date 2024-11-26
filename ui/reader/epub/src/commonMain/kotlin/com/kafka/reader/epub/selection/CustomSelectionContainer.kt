package com.kafka.reader.epub.selection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.text.AnnotatedString
import com.kafka.common.image.Icons

@Composable
fun CustomSelectionContainer(
    modifier: Modifier = Modifier,
    onCopy: ((String) -> Unit)? = null,
    toolbarActions: (@Composable RowScope.(String) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val customToolbar = remember { CustomTextToolbar() }
    val clipboardManager = LocalClipboardManager.current

    CompositionLocalProvider(LocalTextToolbar provides customToolbar) {
        Box(modifier = modifier.fillMaxSize()) {
            SelectionContainer(Modifier.fillMaxSize()) {
                content()
            }

            customToolbar.FloatingToolbar {
                IconButton(
                    onClick = {
                        val text = clipboardManager.getText()?.text ?: ""
                        onCopy?.invoke(text)
                        customToolbar.hide()
                        clipboardManager.setText(AnnotatedString(""))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Translate,
                        contentDescription = "Copy"
                    )
                }
                
                val selectedText = clipboardManager.getText()?.text ?: ""
                if (selectedText.isNotEmpty()) {
                    toolbarActions?.invoke(this, selectedText)
                }
            }
        }
    }
} 