package com.kafka.user.di

import androidx.lifecycle.ViewModel
import com.kafka.user.detail.ItemDetailFragment
import com.kafka.user.detail.ItemDetailViewModel
import com.kafka.user.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [ItemDetailFragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class ContentDetailModule {

    /**
     * Generates an [dagger.android.AndroidInjector] for the [ItemDetailFragment]
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeContentDetailFragment(): ItemDetailFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [ItemDetailViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ItemDetailViewModel::class)
    internal abstract fun bindContentDetailViewModel(viewModel: ItemDetailViewModel): ViewModel
}
