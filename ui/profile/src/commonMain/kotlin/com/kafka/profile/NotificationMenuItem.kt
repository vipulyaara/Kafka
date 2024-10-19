package com.kafka.profile

import androidx.compose.runtime.Composable

@Composable
expect fun NotificationMenuItem(logClick: () -> Unit, dismiss: () -> Unit)
