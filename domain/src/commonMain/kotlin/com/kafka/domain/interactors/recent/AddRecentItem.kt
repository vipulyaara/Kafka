package com.kafka.domain.interactors.recent

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.domain.interactors.GetPrimaryFile
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject

@Inject
class AddRecentItem(
    private val dispatchers: CoroutineDispatchers,
    private val itemDao: ItemDao,
    private val getPrimaryFile: GetPrimaryFile,
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository
) : Interactor<AddRecentItem.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val item = itemDao.getOrNull(params.itemId)
            val user = accountRepository.currentUser
            val primaryFile = getPrimaryFile(params.itemId).getOrThrow()

            if (item != null && primaryFile != null) {
                val recentItem = RecentItem(
                    uid = user.id,
                    fileId = primaryFile.fileId,
                    itemId = item.itemId,
                    title = item.title,
                    coverUrl = item.coverImage,
                    creators = item.creators,
                    mediaType = primaryFile.mediaType,
                    updatedAt = Clock.System.now()
                )

                firestoreGraph.readingListCollection(user.id)
                    .document(item.itemId)
                    .set(recentItem)
            }

//            if (file != null) {
//                val item = CurrentlyReading(
//                    fileId = file.fileId,
//                    uid = user.id,
//                    itemId = file.itemId,
//                    currentPage = 0L,
//                    currentPageOffset = 0L
//                )
//
//                supabase.recentItems.upsert(item) {
//                    filter { CurrentlyReading::fileId eq file.fileId }
//                    filter { CurrentlyReading::itemId eq file.itemId }
//                    filter { CurrentlyReading::uid eq user.id }
//                }
//            }
        }
    }

    data class Params(val itemId: String)
}
