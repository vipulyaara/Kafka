/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */

package com.kafka.ui.components.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DismissableSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(
        hostState = hostState,
        snackbar = {
            SwipeDismissSnackbar(
                data = it,
                onDismiss = { it.dismiss() }
            )
        },
        modifier = modifier
    )
}

/**
 * Wrapper around [Snackbar] to make it swipe-dismissable.
 */
@Composable
fun SwipeDismissSnackbar(
    data: SnackbarData,
    onDismiss: () -> Unit = {},
    snackbar: @Composable (SnackbarData) -> Unit = {
        Snackbar(
            snackbarData = it,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
) {
    Dismissable(onDismiss = onDismiss) {
        snackbar(data)
    }
}

@Composable
fun Dismissable(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
        if (it != SwipeToDismissBoxValue.Settled) {
            onDismiss.invoke()
        }
        true
    })

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {},
        content = { content() }
    )
}
