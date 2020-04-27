package com.kafka.user.home

import androidx.lifecycle.ViewModel
import com.kafka.ui_common.injection.FragmentScoped
import com.kafka.ui_common.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [HomepageFragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class HomepageModule {

    /**
     * Generates an [dagger.android.AndroidInjector] for the [HomepageFragment]
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeHomepageFragment(): HomepageFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [HomepageViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(HomepageViewModel::class)
    internal abstract fun bindRiderHistoryFragment(viewModel: HomepageViewModel): ViewModel
}
