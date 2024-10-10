package com.kafka.user.home.overlays

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kafka.common.extensions.alignCenter
import com.kafka.common.extensions.semiBold
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.ui.components.material.PrimaryButton
import com.kafka.user.R
import com.kafka.user.home.MainViewModel.AppUpdateState
import ui.common.theme.theme.Dimens

@Composable
fun AppUpdate(
    appUpdateConfig: AppUpdateState,
    snackbarManager: SnackbarManager,
    updateApp: () -> Unit,
) {
    val context = LocalContext.current

    if (appUpdateConfig == AppUpdateState.Required) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            DialogContent(update = updateApp)
        }
    }

    LaunchedEffect(appUpdateConfig) {
        if (appUpdateConfig == AppUpdateState.Optional) {
            snackbarManager.addMessage(
                message = context.getString(R.string.app_update_is_available),
                label = context.getString(R.string.update),
                onClick = updateApp
            )
        }
    }
}

@Composable
private fun DialogContent(modifier: Modifier = Modifier, update: () -> Unit) {
    Surface(
        modifier = modifier.padding(Dimens.Gutter),
        shape = RoundedCornerShape(Dimens.Radius12)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.update_available),
                style = MaterialTheme.typography.titleLarge.semiBold().alignCenter(),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(Dimens.Spacing08))

            Text(
                text = stringResource(R.string.force_update_message),
                style = MaterialTheme.typography.bodyMedium.alignCenter()
            )

            Spacer(modifier = Modifier.height(Dimens.Spacing24))

            PrimaryButton(text = stringResource(R.string.update), onClick = update)
        }
    }
}
