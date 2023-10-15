package org.kafka.common.image

import androidx.compose.ui.graphics.vector.ImageVector
import org.kafka.common.image.kafkaicons.Vinyl
import kotlin.collections.List as ____KtList

object KafkaIcons

private var __KafkaIcons: ____KtList<ImageVector>? = null

val KafkaIcons.KafkaIcons: ____KtList<ImageVector>
  get() {
    if (__KafkaIcons != null) {
      return __KafkaIcons!!
    }
    __KafkaIcons= listOf(Vinyl)
    return __KafkaIcons!!
  }
