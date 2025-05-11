package com.kafka.ui.components.item.review

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.ui.components.material.FloatingButton
import kafka.ui.components.generated.resources.Res
import kafka.ui.components.generated.resources.write_a_review
import org.jetbrains.compose.resources.stringResource

@Composable
fun WriteReviewButton(modifier: Modifier = Modifier, writeReview: () -> Unit) {
    FloatingButton(
        text = stringResource(Res.string.write_a_review),
        modifier = modifier,
        onClick = writeReview
    )
}

@Composable
fun WriteReviewFloatingActionButton(
    modifier: Modifier = Modifier,
    writeReview: () -> Unit
) {
    FloatingActionButton(
        onClick = writeReview,
        modifier = modifier,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        IconButton(
            imageVector = Icons.Edit,
            tint = MaterialTheme.colorScheme.onPrimary,
            onClick = writeReview
        )
    }
}
