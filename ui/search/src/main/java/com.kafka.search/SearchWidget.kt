package com.kafka.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.kafka.common.image.Icons
import org.kafka.common.test.testTagUi
import org.kafka.common.widgets.IconResource
import org.kafka.search.R
import ui.common.theme.theme.Dimens

@Composable
fun SearchWidget(
    searchText: String,
    modifier: Modifier = Modifier,
    setSearchText: (String) -> Unit,
    onImeAction: (String) -> Unit
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
                .testTagUi("search_widget"),
            value = searchText,
            placeholder = {
                Row {
                    Text(
                        stringResource(R.string.search),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            trailingIcon = {
                ClearIcon(searchText) {
                    setSearchText("")
                    keyboard?.show()
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
            shape = RoundedCornerShape(Dimens.Spacing08)
        )
    }
}

@Composable
private fun ClearIcon(text: String, onTextCleared: () -> Unit) {
    AnimatedVisibility(
        visible = text.isNotEmpty(),
        enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter),
        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.TopCenter)
    ) {
        IconResource(
            modifier = Modifier
                .testTagUi("search_clear")
                .clickable(onClick = { onTextCleared() })
                .padding(Dimens.Spacing12)
                .size(24.dp),
            imageVector = Icons.X,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(R.string.cd_clear_text)
        )
    }
}

@Composable
private fun SpeechIcon(text: String, onTextCleared: () -> Unit) {
    AnimatedVisibility(
        visible = text.isEmpty(),
        enter = fadeIn() + expandIn(expandFrom = Alignment.BottomCenter),
        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.BottomCenter)
    ) {
        IconResource(
            modifier = Modifier
                .clickable(onClick = { onTextCleared() })
                .padding(Dimens.Spacing12)
                .size(Dimens.Spacing24),
            imageVector = Icons.Mic,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

private val SearchKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrect = false,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Search
)
