package com.kafka.ui

import androidx.ui.core.LayoutCoordinates
import androidx.ui.core.Modifier
import androidx.ui.layout.absolutePadding
import androidx.ui.unit.*

inline val PxBounds.center: PxPosition
    get() = PxPosition((left + right) / 2, (top + bottom) / 2)

inline val LayoutCoordinates.positionInParent: PxPosition
    get() = parentCoordinates?.childToLocal(this, PxPosition.Origin) ?: PxPosition.Origin

inline val LayoutCoordinates.boundsInParent: PxBounds
    get() = PxBounds(positionInParent, size.toPxSize())

fun Modifier.paddingHV(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): Modifier {
    return absolutePadding(left = horizontal, top = vertical, right = horizontal, bottom = vertical)
}
