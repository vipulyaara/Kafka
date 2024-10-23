package com.kafka.search.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.common.testTagUi
import com.kafka.common.widgets.IconResource
import kafka.ui.search.generated.resources.Res
import kafka.ui.search.generated.resources.cd_clear_text
import kafka.ui.search.generated.resources.search
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun SearchWidget(
    searchText: String,
    modifier: Modifier = Modifier,
    setSearchText: (String) -> Unit,
    onImeAction: (String) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .padding(horizontal = Dimens.Spacing12)
            .padding(top = Dimens.Spacing12)
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .testTagUi("search_widget")
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        onImeAction(searchText)
                        true
                    } else {
                        false
                    }
                },
            value = searchText,
            placeholder = {
                Row {
                    Text(
                        stringResource(Res.string.search),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            trailingIcon = {
                AnimatedContent(targetState = searchText, label = "trailing_icon") { text ->
                    if (text.isEmpty()) {
                        SpeechIcon {
                            setSearchText(it)
                            onImeAction(it)
                            keyboard?.hide()
                        }
                    } else {
                        ClearIcon {
                            setSearchText("")
                            keyboard?.show()
                        }
                    }
                }

            },
            keyboardOptions = SearchKeyboardOptions,
            onValueChange = { setSearchText(it) },
            keyboardActions = KeyboardActions(onSearch = {
                keyboard?.hide()
                onImeAction(searchText)
            }),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(Dimens.Spacing08),
            maxLines = 1
        )
    }
}

@Composable
private fun ClearIcon(onTextCleared: () -> Unit) {
    IconResource(
        modifier = Modifier
            .testTagUi("search_clear")
            .clickable(onClick = { onTextCleared() })
            .padding(Dimens.Spacing12)
            .size(24.dp),
        imageVector = Icons.X,
        tint = MaterialTheme.colorScheme.onSurface,
        contentDescription = stringResource(Res.string.cd_clear_text)
    )
}

private val SearchKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrectEnabled = false,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Search
)
