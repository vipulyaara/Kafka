package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.Bookshelf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveBookshelves(
    private val dispatchers: CoroutineDispatchers,
) : SubjectInteractor<Unit, List<Bookshelf>>() {

    override fun createObservable(params: Unit): Flow<List<Bookshelf>> {
        return flowOf(
            listOf(
                Bookshelf("wishlist", "Wishlist", Bookshelf.Type.Wishlist),
                Bookshelf("favorite", "Favorites", Bookshelf.Type.Favorite),
                Bookshelf("completed", "Completed", Bookshelf.Type.Completed),
                Bookshelf("custom", "Poetry", Bookshelf.Type.Custom),
            )
        ).flowOn(dispatchers.io)
    }
}
