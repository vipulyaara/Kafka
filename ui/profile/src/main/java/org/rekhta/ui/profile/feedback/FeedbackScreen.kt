package org.rekhta.ui.profile.feedback

import androidx.activity.compose.BackHandler
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.common.extensions.rememberSavableMutableState
import org.kafka.ui.components.material.OutlinedTextFieldDefaults
import org.kafka.ui.components.material.PrimaryButton
import org.kafka.ui.components.placeholder.PlaceholderHighlight
import org.kafka.ui.components.placeholder.placeholder
import org.kafka.ui.components.placeholder.shimmer
import ui.common.theme.theme.Dimens

@Composable
fun FeedbackScreen(viewModel: FeedbackViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    // todo back press should work out-of-the-box because this is an accompanist.navigation destination
    BackHandler(true) {
        viewModel.onBackPressed()
    }

    Surface(modifier = Modifier.animateContentSize()) {
        Feedback(state) { feedback, email ->
            keyboard?.hide()
            viewModel.sendFeedback(feedback, email)
        }
    }
}

@Composable
private fun Feedback(state: FeedbackViewState, sendFeedback: (String, String) -> Unit) {
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
            text = "Send Feedback",
            modifier = Modifier.then(placeholderModifier),
            enabled = feedback.isNotBlank()
        ) {
            sendFeedback(feedback, email)
        }
    }
}

@Composable
internal fun EmailTextField(
    text: String,
    modifier: Modifier = Modifier,
    setText: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        placeholder = {
            Text(
                text = "Email (Optional)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
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
                text = "Enter your feedback here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
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
