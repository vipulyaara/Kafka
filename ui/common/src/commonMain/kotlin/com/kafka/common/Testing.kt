package com.kafka.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

fun Modifier.testTagUi(tag: String) = this
//    .semantics { testTagsAsResourceId = true }
    .testTag(tag)
