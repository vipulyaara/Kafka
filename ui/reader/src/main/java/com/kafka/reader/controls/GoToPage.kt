package com.kafka.reader.controls

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.alignCenter
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.widgets.shadowMaterial
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
internal fun BoxScope.GoToPage(
    showControls: Boolean,
    currentPage: Int,
    goToPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scaffoldPadding: PaddingValues = scaffoldPadding()
) {
    AnimatedVisibilityFade(
        visible = showControls,
        modifier = modifier.align(Alignment.BottomCenter)
    ) {
        GoToPage(
            currentPage = currentPage,
            goToPage = goToPage,
            modifier = Modifier
                .imePadding()
                .padding(scaffoldPadding)
                .padding(Dimens.Spacing04)
                .width(Dimens.Spacing96)
        )
    }
}


@Composable
internal fun GoToPage(
    currentPage: Int,
    goToPage: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var value by rememberMutableState(currentPage) { currentPage.toString() }
    val keyboard = LocalSoftwareKeyboardController.current

    TextField(
        modifier = modifier.shadowMaterial(Dimens.Elevation04, RoundedCornerShape(50)),
        value = value,
        onValueChange = { value = it },
        textStyle = MaterialTheme.typography.displayMedium.alignCenter(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(onGo = {
            keyboard?.hide()
            goToPage(value.toIntOrNull() ?: currentPage)
        })
    )
}
