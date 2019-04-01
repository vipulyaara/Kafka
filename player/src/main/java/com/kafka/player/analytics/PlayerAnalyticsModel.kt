package com.kafka.player.analytics

class PlayerAnalyticsModel {

    var manifestFetchTime: Long = -1
    var hasManifestBeenFetched: Boolean = false
    var indexFetchTime: Long = -1
    var firstSegmentFetchTime: Long = -1
    var videoPrepareStartTime: Long = -1
    var prepareToFirstSegmentFetchTime: Long = -1
    var isPlayStartSent = false
    var licensePlayInitTime: Long = -1
    var licensePlayFinishTime: Long = -1
    var isFirstTimePlayed = false
    var is403RetryCount: Int = 0
    var licenseCallRoundTripFinishTime: Long = -1
}
