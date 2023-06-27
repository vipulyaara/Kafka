package org.kafka.common.test

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

fun Modifier.testTagUi(tag: String, testTagsAsResource: Boolean = true) = this
    .semantics { testTagsAsResourceId = testTagsAsResource }
    .testTag(tag)
