package com.kafka.textreader

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ResourceType {
    @Parcelize
    data class Local(val uri: Uri) : ResourceType(), Parcelable
}
