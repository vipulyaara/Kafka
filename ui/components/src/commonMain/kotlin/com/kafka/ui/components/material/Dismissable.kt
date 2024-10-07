@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.ui.components.material

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconResource
import ui.common.theme.theme.Dimens

@Composable
fun SwipeToDelete(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    when (dismissState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> { onDismiss() }
        else -> {}
    }

    //todo: kmp crashes on this and DismissableSnackbar
    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = { DeleteAction(dismissState.progress) },
        content = { content() }
    )
}

@Composable
private fun DeleteAction(progress: Float) {
    val color = MaterialTheme.colorScheme.error.copy(alpha = progress * 2)

    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color)
            .padding(horizontal = Dimens.Spacing48),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconResource(
            imageVector = Icons.Delete,
            tint = MaterialTheme.colorScheme.onError
        )
    }
}
