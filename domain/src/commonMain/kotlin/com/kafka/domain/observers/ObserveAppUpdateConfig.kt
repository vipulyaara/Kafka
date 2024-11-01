package com.kafka.domain.observers

import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.AppUpdateConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAppUpdateConfig @Inject constructor(
    private val firestoreGraph: FirestoreGraph
) : SubjectInteractor<Unit, AppUpdateConfig>() {
    override fun createObservable(params: Unit): Flow<AppUpdateConfig> {
        return firestoreGraph.appUpdateConfig.snapshots
            .map { it.data() }
    }
}
