@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.ui.components.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.kafka.ui.components.scaffoldPadding
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import ui.common.theme.theme.Dimens

@Composable
fun RichTextEditorFull(textState: RichTextState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(scaffoldPadding())
    ) {
        RichTextEditor(
            textState = textState,
            placeholder = "Write a review here. Remember to stay positive and non-abusive towards our community.",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = Dimens.Gutter)
        )

        EditorActions(textState = textState)
    }
}

@Composable
fun RichTextEditor(
    textState: RichTextState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    contentPadding: PaddingValues = PaddingValues()
) {
    OutlinedRichTextEditor(
        state = textState,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = {
            placeholder?.let {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        contentPadding = contentPadding,
        colors = RichTextEditorDefaults.outlinedRichTextEditorColors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        )
    )
}
