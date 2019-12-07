package com.kafka.data.data.config

import com.kafka.data.data.config.logging.Logger
import com.kafka.data.data.config.logging.TimberLogger
import com.kafka.data.util.AppCoroutineDispatchers
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [DataModuleBinds::class])
class DataModule {
    @Provides
    fun provideAppCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )
}

@Module
abstract class DataModuleBinds {
    @Singleton
    @Binds
    abstract fun provideLogger(bind: TimberLogger): Logger

}