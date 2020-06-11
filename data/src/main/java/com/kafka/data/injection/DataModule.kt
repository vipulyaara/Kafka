package com.kafka.data.injection

import com.data.base.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(ApplicationComponent::class)
@Module(includes = [DatabaseDaoModule::class, ServiceModule::class])
class DataModule {
    @Provides
    fun provideAppCoroutineDispatchers() =
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
}
