package org.kafka.domain.observers

import com.kafka.data.dao.AuthDao
import com.kafka.data.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveUser @Inject constructor(
    private val authDao: AuthDao,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<Unit, User?>() {

    override fun createObservable(params: Unit): Flow<User?> {
        return authDao.observeUser().flowOn(dispatchers.io)
    }
}
