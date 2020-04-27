package com.kafka.player

import androidx.lifecycle.ViewModel
import com.kafka.player.ui.PlayerFragment
import com.kafka.player.ui.PlayerViewModel
import com.kafka.ui_common.injection.FragmentScoped
import com.kafka.ui_common.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class PlayerModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeContentDetailFragment(): PlayerFragment

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    internal abstract fun bindContentDetailViewModel(viewModel: PlayerViewModel): ViewModel
}
