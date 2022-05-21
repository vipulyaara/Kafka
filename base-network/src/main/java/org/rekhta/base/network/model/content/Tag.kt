package org.rekhta.base.network.model.content

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Immutable
data class Tag(
    @SerialName("TI")
    val ti: String,
    @SerialName("N")
    val name: String? = null,
    @SerialName("TS")
    val ts: String? = null,
    @SerialName("TC")
    val tc: String? = null
)

fun String.toHashTag() = replace(" ", "_").toLowerCase(Locale.ROOT)
