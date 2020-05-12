package com.kafka.user.injection

import com.kafka.player.injection.PlayerFragmentModule
import com.kafka.reader.injection.ReaderFragmentModule
import com.kafka.search.inejction.SearchModule
import com.kafka.ui_common.injection.ActivityScoped
import com.kafka.user.MainActivity
import com.kafka.user.detail.ContentDetailModule
import com.kafka.user.home.HomepageModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            HomepageModule::class,
            ContentDetailModule::class,
            SearchModule::class,
            PlayerFragmentModule::class,
            ReaderFragmentModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}
