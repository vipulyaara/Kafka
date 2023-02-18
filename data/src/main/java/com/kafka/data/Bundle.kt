package com.kafka.data

import android.os.Bundle

operator fun Bundle?.plus(other: Bundle?) =
    this.apply { (this ?: Bundle()).putAll(other ?: Bundle()) }
