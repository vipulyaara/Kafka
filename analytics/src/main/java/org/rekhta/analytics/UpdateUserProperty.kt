package org.rekhta.analytics

import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UpdateUserProperty @Inject constructor(
    @ProcessLifetime override val scope: CoroutineScope,
    override val logger: Logger,
    override val event: ContentEventProvider
) : LoggingInteractor<ContentEventProvider>()
