@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.AppMessage
import com.kafka.data.platform.device.UserCountryRepository
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.appMessageShownKey
import dev.gitlive.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAppMessage @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val preferencesStore: PreferencesStore,
    private val userCountryRepository: UserCountryRepository,
    private val dispatchers: CoroutineDispatchers
) : SubjectInteractor<Unit, AppMessage?>() {

    override fun createObservable(params: Unit): Flow<AppMessage?> {
        return firestoreGraph.appMessageConfig.snapshots
            .map<DocumentSnapshot, AppMessage> { it.data() }
            .map { it.copy(text = it.text.replace("||", "\n")) }
            .flatMapLatest {
                // message was not already shown
                preferencesStore.get(appMessageShownKey(it.id), false)
                    .map { shown -> it.takeIf { !shown } }
            }
            .map { message -> message?.takeIf { message.enabled } }
            .map { message ->
                // message is intended for user's country
                message?.takeIf { message.isInCountry(userCountryRepository.getUserCountry()) }
            }
            .flowOn(dispatchers.io)
    }

    private fun AppMessage.isInCountry(country: String?): Boolean {
        return countries.isEmpty() || country in countries.map { it.lowercase() }
    }
}
