package com.kafka.user.config.di

import androidx.lifecycle.ViewModelProvider
import com.coyote.android.ui.common.CoyoteViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Module used to define the connection between the framework's [ViewModelProvider.Factory] and
 * our own implementation: [CoyoteViewModelFactory].
 */
@Module
@Suppress("UNUSED")
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: CoyoteViewModelFactory):
        ViewModelProvider.Factory
}
