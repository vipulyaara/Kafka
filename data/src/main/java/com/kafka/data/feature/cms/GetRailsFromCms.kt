package com.kafka.data.feature.cms

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.ResourcePath
import com.kafka.data.feature.detail.ContentDetailRepository
import com.kafka.data.util.AppCoroutineDispatchers
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author Vipul Kumar; dated 03/03/19.
 */
class GetRailsFromCms constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: ContentDetailRepository
) : SubjectInteractor<GetRailsFromCms.Params, GetRailsFromCms.ExecuteParams, DocumentSnapshot>() {

    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun execute(params: Params, executeParams: ExecuteParams) {
    }

    override fun createObservable(params: Params): Flowable<DocumentSnapshot> {
        return RxFirestore.observeDocumentRef(
            DocumentReference.forPath(
                ResourcePath.EMPTY,
                FirebaseFirestore.getInstance()
            )
        )
    }

    data class Params(val contentId: String)

    data class ExecuteParams(val id: Long = 0)
}
