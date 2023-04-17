package org.kafka.domain.observers

import com.kafka.data.entities.Homepage
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksBySubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class SubjectHomepageItemProvider @Inject constructor(
    private val observeQueryItems: ObserveQueryItems,
    private val dispatcherProvider: AppCoroutineDispatchers,
) : SubjectInteractor<Unit, List<Homepage.CollectionItem>>() {

    override fun createObservable(params: Unit): Flow<List<Homepage.CollectionItem>> {
        val subjects = listOf(
            Subject("librivox", ArchiveQuery().booksBySubject("librivox")),
            Subject("adbi-duniya", ArchiveQuery().booksBySubject("adbi-duniya")),
            Subject("kafka-archives", ArchiveQuery().booksBySubject("kafka archives")),
            Subject("urdu", ArchiveQuery().booksBySubject("urdu")),
            Subject("हिंदी", ArchiveQuery().booksBySubject("हिंदी")),
        )
        return combine(
            subjects.map { observeQueryItems.execute(ObserveQueryItems.Params(it.query)) }
        ) { rows ->
            buildList {
                rows.filter { it.isNotEmpty() }.forEachIndexed { index, items ->
                    add(Homepage.Label(subjects[index].name))

                    val pairs = items.chunked(3) {
                        Homepage.ItemPair(it[0], it.getOrNull(1), it.getOrNull(2))
                    }

                    add(Homepage.Row(pairs, index.toString()))
                }
            }
        }.flowOn(dispatcherProvider.io)
    }

    data class Subject(val name: String, val query: ArchiveQuery)
}
