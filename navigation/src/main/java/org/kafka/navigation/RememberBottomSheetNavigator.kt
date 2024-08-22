package org.kafka.navigation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.navigation.BottomSheetNavigator
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SpringSpec(),
    skipHalfExpanded: Boolean = true,
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = animationSpec,
        confirmValueChange = { true },
        skipHalfExpanded = skipHalfExpanded,
    )

    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}
