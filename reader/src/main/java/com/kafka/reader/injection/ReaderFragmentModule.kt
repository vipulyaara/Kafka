package com.kafka.reader.injection

import androidx.lifecycle.ViewModel
import com.kafka.reader.ReaderFragment
import com.kafka.reader.ReaderViewModel
import com.kafka.ui_common.injection.FragmentScoped
import com.kafka.ui_common.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class ReaderFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeFragment(): ReaderFragment

    @Binds
    @IntoMap
    @ViewModelKey(ReaderViewModel::class)
    internal abstract fun bindViewModel(viewModel: ReaderViewModel): ViewModel
}
