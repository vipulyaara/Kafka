package com.kafka.domain.observers

import androidx.datastore.preferences.core.intPreferencesKey
import com.kafka.data.prefs.PreferencesStore
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.shareAppIndex
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveShareAppIndex @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val remoteConfig: RemoteConfig,
    private val dispatchers: CoroutineDispatchers
) : SubjectInteractor<Unit, Int>() {

    override fun createObservable(params: Unit): Flow<Int> {
        return preferencesStore.get(intPreferencesKey("item_opens"), 0).map {
            if (it > 20) {
                remoteConfig.shareAppIndex().toInt()
            } else {
                -1
            }
        }.flowOn(dispatchers.io)
    }
}
