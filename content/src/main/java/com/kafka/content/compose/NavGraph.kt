package com.kafka.content.compose

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.kafka.ui.Navigator
import kotlinx.android.parcel.Parcelize

/**
 * Models the screens in the app and any arguments they require.
 */
sealed class Destination : Parcelable {
    @Parcelize
    object Home : Destination()

    @Immutable
    @Parcelize
    data class ItemDetail(val itemId: String) : Destination()
}

/**
 * Models the navigation actions in the app.
 */
class Actions(navigator: Navigator<Destination>) {
    val itemDetail: (String) -> Unit = { id: String ->
        navigator.navigate(Destination.ItemDetail(id))
    }
    val upPress: () -> Unit = {
        navigator.back()
    }
}
