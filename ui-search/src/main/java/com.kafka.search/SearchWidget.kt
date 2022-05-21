package org.rekhta.ui.search

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.kafka.common.Icons
import org.kafka.common.widgets.IconResource
import org.kafka.common.widgets.LoadImage
import org.rekhta.homepage.R
import ui.common.theme.theme.iconPrimary
import ui.common.theme.theme.textSecondary

@Composable
fun SearchWidget(
    searchText: TextFieldValue,
    modifier: Modifier = Modifier,
    setSearchText: (TextFieldValue) -> Unit,
    onImeAction: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = searchText,
            placeholder = {
                Row {
                    Text(
                        "Search...",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.textSecondary
                    )
                }
            },
            trailingIcon = {
                ClearIcon(searchText.text) {
                    setSearchText(TextFieldValue(""))
                    keyboard?.show()
                }
                SpeechIcon(searchText.text) {}
            },
            keyboardOptions = SearchKeyboardOptions,
            onValueChange = { setSearchText(it) },
            keyboardActions = KeyboardActions(onSearch = {
                onImeAction(searchText.text)
                keyboard?.hide()
            }),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onBackground
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun SearchIcon(text: String, onTextChange: (String) -> Unit) {
    AnimatedVisibility(visible = text.isNotEmpty()) {
        LoadImage(
            modifier = Modifier
                .clickable(onClick = { onTextChange(text) })
                .padding(12.dp)
                .size(24.dp),
            data = R.drawable.ic_search
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
                .clickable(onClick = { onTextCleared() })
                .padding(12.dp)
                .size(24.dp),
            imageVector = Icons.X,
            tint = MaterialTheme.colorScheme.iconPrimary
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
                .padding(12.dp)
                .size(24.dp),
            imageVector = Icons.Mic,
            tint = MaterialTheme.colorScheme.iconPrimary
        )
    }
}

private val SearchKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrect = false,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Search
)
