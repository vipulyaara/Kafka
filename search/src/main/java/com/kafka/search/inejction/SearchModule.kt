package com.kafka.search.inejction

import androidx.lifecycle.ViewModel
import com.kafka.search.ui.SearchViewModel
import com.kafka.ui_common.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@Suppress("UNUSED")
abstract class SearchModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindContentDetailViewModel(viewModel: SearchViewModel): ViewModel
}
