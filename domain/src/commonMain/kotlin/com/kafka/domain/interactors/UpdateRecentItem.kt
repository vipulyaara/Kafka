package com.kafka.domain.interactors

import com.kafka.base.Service
import com.kafka.base.appRecentItems
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.SupabaseDb
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import javax.inject.Inject

class UpdateRecentItem @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository,
    private val supabaseDb: SupabaseDb
) : Interactor<RecentItem>() {
    override suspend fun doWork(params: RecentItem) {
        if (appRecentItems == Service.Archive) {
            val document = firestoreGraph.recentItemsCollection.document(params.fileId)
            document.set(params)
            debug { "UpdateRecentItem updated" }
        } else {
            val uid = accountRepository.currentFirebaseUser?.uid

            if (uid != null) {
                supabaseDb.recentItems.upsert(params) {
                    filter { eq("file_id", params.fileId) }
                    filter { eq("uid", uid) }
                }
            }
        }
    }
}
