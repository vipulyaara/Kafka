package org.kafka.domain.interactors

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kafka.data.dao.AuthDao
import com.kafka.data.entities.User
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class LoginUser @Inject constructor(
    private val auth: FirebaseAuth,
    private val authDao: AuthDao,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<LoginUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val user = auth.signInWithEmailAndPassword(params.email, params.password).await().user
            user?.let { authDao.insert(it.asUser()) } ?: error("Login Failed")
        }
    }

    data class Params(val email: String, val password: String)
}

fun FirebaseUser.asUser() = User(
    id = uid,
    displayName = displayName.orEmpty(),
    imageUrl = photoUrl?.toString(),
)
