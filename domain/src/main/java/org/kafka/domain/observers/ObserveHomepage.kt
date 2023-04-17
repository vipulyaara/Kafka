package org.kafka.domain.observers

import com.google.firebase.firestore.ktx.snapshots
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.Item
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.observers.library.ObserveFavorites
import javax.inject.Inject

class ObserveHomepage @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val observeRecentItems: ObserveRecentItems,
    private val observeFavorites: ObserveFavorites,
    private val observeQueryItems: ObserveQueryItems,
    private val subjectHomepageItemProvider: SubjectHomepageItemProvider,
    firestoreGraph: FirestoreGraph
) : SubjectInteractor<Unit, Homepage>() {

    override fun createObservable(params: Unit): Flow<Homepage> {
        return combine(
            homepageQueryItems,
            subjectHomepageItemProvider.execute(Unit),
            observeRecentItems.execute(Unit),
            observeFavorites.execute(Unit),
        ) { queryItems, subjectRows, recentItems, _ ->
            /*
            * ugly code to remove duplicate items from query and subject rows.
            * todo: move this logic to firestore
            * */
            val subjectItems = subjectRows.filterIsInstance<Homepage.Row>()
                .map { it.items.map { it.all() }.flatten() }
                .flatten().filterNotNull()
            val queryLabel = if (queryItems.isEmpty()) null else Homepage.Label("Editor's choice")
            val editorsChoiceItems =
                queryItems.filter { queryItem -> subjectItems.none { it.itemId == queryItem.itemId } }
            val column =
                if (editorsChoiceItems.isEmpty()) null else Homepage.Column(editorsChoiceItems)
            val items = subjectRows + queryLabel + column
            Homepage(recentItems, items.filterNotNull())
        }.flowOn(appCoroutineDispatchers.io)
    }

    private val homepageIds =
        firestoreGraph.homepageCollection.snapshots().map { it.get("ids").toString() }

    private val homepageQueryItems = homepageIds
        .flatMapLatest {
            val query = ArchiveQuery().booksByIdentifiers(it)
            observeQueryItems.execute(ObserveQueryItems.Params(query))
        }

    private suspend fun List<Item>.sortByIds() = homepageIds.first().split(", ")
        .mapNotNull { this.associateBy { it.itemId }[it] }
}
