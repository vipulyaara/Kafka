package org.kafka.domain.interactors

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val updateItems: UpdateItems,
    private val firestoreGraph: FirestoreGraph
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val homepageIds = firestoreGraph.homepageCollection.get().await().get("ids").toString()
            val query = ArchiveQuery().booksByIdentifiers(homepageIds)
            updateItems.execute(UpdateItems.Params(query))
        }
    }
}
