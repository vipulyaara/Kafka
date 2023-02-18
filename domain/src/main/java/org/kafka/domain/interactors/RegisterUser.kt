package org.kafka.domain.interactors

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.kafka.data.dao.AuthDao
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val auth: FirebaseAuth,
    private val authDao: AuthDao,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<RegisterUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            auth.createUserWithEmailAndPassword(params.email, params.password)
                .await().user?.let {
                    updateProfile(params.name, it)
                    authDao.insert(it.asUser())
                } ?: error("Signup Failed")
        }
    }

    private suspend fun updateProfile(name: String, user: FirebaseUser) {
        UserProfileChangeRequest.Builder()
            .setDisplayName(name).build()
            .let { user.updateProfile(it).await() }
    }

    data class Params(val email: String, val password: String, val name: String)
}
