package com.kafka.player.helper

import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.kafka.data.extensions.debug
import java.util.*

class TrackSelectionHelper(
    private val trackSelector: DefaultTrackSelector,
    private val resolutionListener: ResolutionListener?
) {
    private var trackGroups: TrackGroupArray? = null
    private var override: DefaultTrackSelector.SelectionOverride? = null
    private var rendererIndex: Int = 0
    private val availableFormats = ArrayList<Format>()
    private var selectedFormat: Format? = null

    fun setAvailableBitRates(rendererIndex: Int, trackGroups: TrackGroupArray?) {
        this.rendererIndex = rendererIndex
        this.trackGroups = trackGroups
        availableFormats.clear()
        if (trackGroups == null) {
            return
        }
        for (groupIndex in 0 until trackGroups.length) {
            val group = trackGroups.get(groupIndex)
            for (trackIndex in 0 until group.length) {
                val format = group.getFormat(trackIndex)
                availableFormats.add(format)
            }
        }
        availableFormats.sortBy {
            it.bitrate
        }
    }

    private fun setOverride(group: Int, enableRandomAdaptation: Boolean, vararg tracks: Int) {
        override = if (enableRandomAdaptation) {
            DefaultTrackSelector.SelectionOverride(group, *getTracksAdding(availableFormats))
        } else {
            DefaultTrackSelector.SelectionOverride(group, *tracks)
        }
        trackSelector.setParameters(
            trackSelector
                .buildUponParameters()
                .setSelectionOverride(rendererIndex, trackGroups, override)
        )
    }

    fun setBitrates(bitrates: IntArray) {
        val trackIndexes = IntArray(bitrates.size)
        for (i in bitrates.indices) {
            val mostSuitable = getMostSuitableTrackBitrate(bitrates[i])
            val trackIndex = getSuitableTrackIndex(mostSuitable)
            trackIndexes[i] = trackIndex
            debug { "set track Index  for exo player := $trackIndex, mostSuitable selected = $mostSuitable" }
        }
        setOverride(0, false, *trackIndexes)
    }

    fun setBitRate(bitRateMax: Int) {
        if (availableFormats.size > 1) {
            val groupIndex = 0 //  track group of all track groups have one element only
            val mostSuitable = getMostSuitableTrackBitrate(bitRateMax)
            if (mostSuitable == 0) {
                return
            }
            val trackIndex = getSuitableTrackIndex(mostSuitable)
            selectedFormat = availableFormats[trackIndex]
            setOverride(groupIndex, bitRateMax == 0, trackIndex)
            debug { "set track Index  for exo player := $trackIndex, mostSuitable selected = $mostSuitable and max bitRate = $bitRateMax" }
        } else {
            debug { "multiple bit rates not found." }
        }
    }

    private fun getSuitableTrackIndex(mostSuitableTrackBitrate: Int): Int {
        var trackIndex = 0
        for (i in availableFormats.indices) {
            if (availableFormats[i].bitrate.compareTo(mostSuitableTrackBitrate) == 0) {
                trackIndex = i
                break
            }
        }
        return trackIndex
    }

    private fun getMostSuitableTrackBitrate(selectedBitrate: Int): Int {
        var mostSuitableBitrate = 0
        if (availableFormats.size == 0) {
            debug { "bitrate not found." }
            return mostSuitableBitrate
        }
        val min = availableFormats[0].bitrate
        val max = availableFormats[availableFormats.size - 1].bitrate
        if (min > selectedBitrate) {
            mostSuitableBitrate = min
        } else if (max < selectedBitrate) {
            mostSuitableBitrate = max
        } else {
            for (i in 1 until availableFormats.size) {

                if (availableFormats[i - 1].bitrate <= selectedBitrate && selectedBitrate <= availableFormats[i].bitrate) {
                    val value = (availableFormats[i - 1].bitrate + availableFormats[i].bitrate) / 2
                    mostSuitableBitrate = if (selectedBitrate > value) {
                        availableFormats[i].bitrate
                    } else {
                        availableFormats[i - 1].bitrate
                    }
                    break
                }
            }
        }
        return mostSuitableBitrate
    }

    private fun getTracksAdding(trackGroupArrayList: ArrayList<Format>): IntArray {
        val tracks = IntArray(trackGroupArrayList.size)
        for (i in trackGroupArrayList.indices) {
            tracks[i] = i
        }
        return tracks
    }
}
