package com.kafka.user.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.kafka.user.R
import com.kafka.common.extensions.alignCenter
import com.kafka.common.extensions.semiBold
import com.kafka.ui.components.material.PrimaryButton
import ui.common.theme.theme.Dimens

@Composable
fun ForceUpdateDialog(show: Boolean, onDismissRequest: () -> Unit = {}, update: () -> Unit) {
    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            DialogContent(update = update)
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
