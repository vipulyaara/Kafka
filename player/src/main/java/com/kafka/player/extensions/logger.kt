package com.kafka.player.extensions

import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.config.logging.Logger
import org.kodein.di.generic.instance

internal val logger: Logger by kodeinInstance.instance()
