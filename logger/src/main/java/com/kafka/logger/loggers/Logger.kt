package com.kafka.logger.loggers

import android.os.Bundle

interface Logger { fun log(eventInfo: EventInfo) }

interface Event

typealias EventInfo = Pair<String, Bundle>

operator fun EventInfo.invoke(pair: Pair<String, Bundle>) = pair

