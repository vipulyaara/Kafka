package com.kafka.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.common.test.testTagUi
import com.kafka.common.widgets.IconResource
import com.kafka.search.R
import ui.common.theme.theme.Dimens
import java.util.Locale

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
            shape = RoundedCornerShape(Dimens.Spacing08)
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
        contentDescription = stringResource(R.string.cd_clear_text)
    )
}

@Composable
private fun SpeechIcon(onText: (String) -> Unit) {
    val context = LocalContext.current

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                onText(results[0])
            }
        }
    }

    fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.speech_prompt))
        }
        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    IconResource(
        modifier = Modifier
            .padding(Dimens.Spacing12)
            .size(Dimens.Spacing24),
        imageVector = Icons.Mic,
        tint = MaterialTheme.colorScheme.onSurface,
        onClick = { promptSpeechInput() },
    )
}

private val SearchKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrectEnabled = false,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Search
)
