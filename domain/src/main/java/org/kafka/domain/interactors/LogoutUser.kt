package org.kafka.domain.interactors

import com.google.firebase.auth.FirebaseAuth
import com.kafka.data.dao.AuthDao
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val auth: FirebaseAuth,
    private val authDao: AuthDao,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(appCoroutineDispatchers.io) {
            authDao.deleteAll()
            auth.signOut()
        }
    }
}
