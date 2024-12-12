package com.kafka.ui.components.item.review

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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