package com.kafka.domain.interactors.account

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.Supabase
import io.github.jan.supabase.auth.providers.Google
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInWithGoogle @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val supabase: Supabase
) : Interactor<Any?, Unit>() {

    override suspend fun doWork(params: Any?) {
        withContext(coroutineDispatchers.io) {
            //todo: check how this works
            supabase.auth.signInWith(Google)
        }
    }
}
