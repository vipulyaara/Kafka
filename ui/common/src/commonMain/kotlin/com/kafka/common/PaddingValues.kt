/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package com.kafka.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/** Calculates the end padding with [LocalLayoutDirection]. **/
@Composable
internal fun PaddingValues.calculateEndPadding(): Dp {
    return calculateEndPadding(LocalLayoutDirection.current)
}

/** Calculates the start padding with [LocalLayoutDirection]. **/
@Composable
internal fun PaddingValues.calculateStartPadding(): Dp {
    return calculateStartPadding(LocalLayoutDirection.current)
}

/** Copies the given [PaddingValues]. **/
@Composable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(),
    bottom: Dp = calculateBottomPadding()
): PaddingValues {
    return PaddingValues(start, top, end, bottom)
}

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    return copy(
        start = calculateStartPadding() + other.calculateStartPadding(),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding() + other.calculateEndPadding(),
        bottom = calculateBottomPadding() + other.calculateBottomPadding()
    )
}

fun PaddingValues.minus(
    other: PaddingValues,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): PaddingValues = PaddingValues(
    start = (calculateStartPadding(layoutDirection) - other.calculateStartPadding(layoutDirection)).coerceAtLeast(
        0.dp
    ),
    top = (calculateTopPadding() - other.calculateTopPadding()).coerceAtLeast(0.dp),
    end = (calculateEndPadding(layoutDirection) - other.calculateEndPadding(layoutDirection)).coerceAtLeast(
        0.dp
    ),
    bottom = (calculateBottomPadding() - other.calculateBottomPadding()).coerceAtLeast(0.dp),
)
