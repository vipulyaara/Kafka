package com.kafka.data.feature

import com.kafka.data.entities.DownloadItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import dagger.Reusable
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Reusable
class DownloadsRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository
) {
    suspend fun addDownload(downloadItem: DownloadItem) {
        accountRepository.currentFirebaseUser?.uid?.let {
            firestoreGraph.getDownloadsCollection(it).document(downloadItem.id)
                .set(downloadItem)
        }?.await()
    }

    suspend fun removeDownload(id: String) {
        accountRepository.currentFirebaseUser?.uid?.let {
            firestoreGraph.getDownloadsCollection(it).document(id).delete()
        }?.await()
    }
}
