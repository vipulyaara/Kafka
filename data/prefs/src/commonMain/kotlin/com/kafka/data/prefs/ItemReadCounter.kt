package com.kafka.data.prefs

import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineScope
import com.kafka.base.ApplicationScope
import com.kafka.base.ProcessLifetime
import javax.inject.Inject

@ApplicationScope
class ItemReadCounter @Inject constructor(
    preferencesStore: PreferencesStore,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
) {
    //todo: change name
    private val itemOpensPrefKey = intPreferencesKey("item_opens")

    private val itemOpens = preferencesStore.getStateFlow(
        keyName = itemOpensPrefKey, scope = coroutineScope, initialValue = 0
    )

    fun incrementItemOpenCount() {
        itemOpens.value++
    }

    val totalItemOpens: Int
        get() = itemOpens.value
}
