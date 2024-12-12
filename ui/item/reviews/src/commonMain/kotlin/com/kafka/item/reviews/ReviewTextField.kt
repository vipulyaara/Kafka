package com.kafka.item.reviews

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.kafka.common.testTagUi
import com.kafka.ui.components.material.OutlinedTextFieldDefaults
import com.kafka.ui.components.search.SpeechIcon
import ui.common.theme.theme.Dimens

@Composable
fun ReviewTextField(
    text: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .testTagUi("search_widget"),
            value = text,
            placeholder = {
                Text(
                    text = "Write a review...",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            trailingIcon = {
                AnimatedContent(targetState = text, label = "trailing_icon") { text ->
                    if (text.isEmpty()) {
                        SpeechIcon {
                            onTextChange(it)
                            keyboard?.hide()
                        }
                    }
                }
            },
            keyboardOptions = SearchKeyboardOptions,
            onValueChange = { onTextChange(it) },
            keyboardActions = KeyboardActions(),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = OutlinedTextFieldDefaults.colors(),
            shape = RoundedCornerShape(Dimens.Spacing08),
        )
    }
}

private val SearchKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrectEnabled = false,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Done
)
