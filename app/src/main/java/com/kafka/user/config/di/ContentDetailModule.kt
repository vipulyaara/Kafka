package com.kafka.user.config.di

import androidx.lifecycle.ViewModel
import com.kafka.user.detail.ContentDetailFragment
import com.kafka.user.detail.ContentDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [ContentDetailFragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class ContentDetailModule {

    /**
     * Generates an [dagger.android.AndroidInjector] for the [ContentDetailFragment]
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeContentDetailFragment(): ContentDetailFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [ContentDetailViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ContentDetailViewModel::class)
    internal abstract fun bindContentDetailViewModel(viewModel: ContentDetailViewModel): ViewModel
}
