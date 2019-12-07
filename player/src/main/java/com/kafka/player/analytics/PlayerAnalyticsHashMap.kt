package com.kafka.player.analytics

import android.text.TextUtils
import com.kafka.data.extensions.e
import java.util.*

class PlayerAnalyticsHashMap : HashMap<String, String?>() {

    override fun put(key: String, value: String?): String? {
        if (value != null && !TextUtils.isEmpty(value.toString())) {
            return super.put(key, value)
        } else {
            e {"$TAG null info is trying to added key : $key value $value" }
        }
        return value
    }

    override fun putAll(from: Map<out String, String?>) {
        for((key, value) in from.entries) {
            put(key, value)
        }
    }

    companion object {
        var TAG = PlayerAnalyticsHashMap::class.java.simpleName
    }
}
