package com.kafka.player.helper

import com.google.android.exoplayer2.Format

interface ResolutionListener {
    fun updateResolution(format: Format?)
}
