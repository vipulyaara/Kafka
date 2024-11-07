package com.kafka.item.report

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.extensions.rememberSavableMutableState
import com.kafka.ui.components.material.OutlinedTextFieldDefaults
import com.kafka.ui.components.material.PrimaryButton
import com.kafka.ui.components.placeholder.PlaceholderHighlight
import com.kafka.ui.components.placeholder.placeholder
import com.kafka.ui.components.placeholder.shimmer
import ui.common.theme.theme.Dimens

@Composable
fun ReportContentScreen(viewModel: ReportContentViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier.animateContentSize()) {
        ReportContent(state) { feedback, email ->
            keyboard?.hide()
            viewModel.report(feedback, email)
        }
    }
}

@Composable
private fun ReportContent(state: ReportContentState, report: (String, String) -> Unit) {
    val placeholderModifier = Modifier.placeholder(
        visible = state.isLoading,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(Dimens.Spacing12),
        highlight = PlaceholderHighlight.shimmer()
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing20)
            .navigationBarsPadding()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
    ) {
        var email by rememberSavableMutableState(init = { "" })
        var feedback by rememberSavableMutableState(init = { "" })

        LaunchedEffect(state.email) {
            email = state.email.orEmpty()
        }

        EmailTextField(
            text = email,
            modifier = Modifier.then(placeholderModifier),
            setText = { email = it })

        FeedbackTextField(
            text = feedback,
            modifier = Modifier.then(placeholderModifier),
            setText = { feedback = it })

        PrimaryButton(
            text = "Report Content for Copyright",
            modifier = Modifier.then(placeholderModifier),
            enabled = feedback.isNotBlank()
        ) {
            report(feedback, email)
        }
    }
}

@Composable
internal fun EmailTextField(
    text: String,
    modifier: Modifier = Modifier,
    setText: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        placeholder = {
            Text(
                text = "Your email (Required)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        onValueChange = { setText(it) },
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(),
        shape = RoundedCornerShape(Dimens.Spacing08)
    )
}

@Composable
internal fun FeedbackTextField(
    text: String,
    modifier: Modifier = Modifier,
    setText: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        placeholder = {
            Text(
                text = "Enter the copyright violation details here. Include the name and contact details of the copyright holder.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        onValueChange = { setText(it) },
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(),
        shape = RoundedCornerShape(Dimens.Spacing08),
        minLines = 5
    )
}
