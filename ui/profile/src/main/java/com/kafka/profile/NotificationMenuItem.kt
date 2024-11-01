package com.kafka.profile

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.kafka.common.extensions.ProvideInteractiveEnforcement
import com.kafka.common.extensions.rememberSavableMutableState
import com.kafka.common.image.Icons
import kafka.ui.profile.generated.resources.Res
import kafka.ui.profile.generated.resources.notifications
import kafka.ui.profile.generated.resources.notifications_toggle_description
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun NotificationMenuItem(logClick: () -> Unit, dismiss: () -> Unit) {
    val context = LocalContext.current
    var notificationsEnabled by rememberSavableMutableState { false }

    LaunchedEffect(context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationsEnabled = nm.areNotificationsEnabled()
    }

    fun openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }
    }

    if (!notificationsEnabled) {
        MenuItem(
            text = stringResource(Res.string.notifications),
            description = stringResource(Res.string.notifications_toggle_description),
            icon = Icons.Bell,
            onClick = {
                dismiss()
                openNotificationSettings()
            },
            endContent = {
                ProvideInteractiveEnforcement {
                    Switch(
                        checked = false,
                        onCheckedChange = {
                            dismiss()
                            openNotificationSettings()
                        }
                    )
                }
            }
        )
    }
}
