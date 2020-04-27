package com.kafka.search.inejction

import androidx.lifecycle.ViewModel
import com.kafka.search.ui.SearchFragment
import com.kafka.search.ui.SearchViewModel
import com.kafka.ui_common.injection.FragmentScoped
import com.kafka.ui_common.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
@Suppress("UNUSED")
abstract class SearchModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeContentDetailFragment(): SearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindContentDetailViewModel(viewModel: SearchViewModel): ViewModel
}
