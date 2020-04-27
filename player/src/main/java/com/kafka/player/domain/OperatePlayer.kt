package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class OperatePlayer @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope
) : Interactor<OperatePlayer.Command>() {

    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Command) {
        when (params) {
            is Command.Play -> {
            }
            else -> {
            }
        }
    }

    sealed class Command {
        data class Play(val itemId: String) : Command()
    }
}
