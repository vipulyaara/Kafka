package com.kafka.user.config.di

import android.content.Context
import com.kafka.data.data.config.PerActivity
import com.kafka.user.feature.MainActivity
import com.kafka.user.feature.detail.ContentDetailFragment
import com.kafka.user.feature.home.HomepageFragment
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.kafka.user.config.di.AssistedInject_HomepageAssistedModule

@Module
internal abstract class HomeBuilder {
    @ContributesAndroidInjector(
        modules = [
            HomeModule::class,
            HomepageBuilder::class
        ]
    )
    internal abstract fun homeActivity(): MainActivity
}

@Module(includes = [HomeModuleBinds::class])
class HomeModule {
}

@Module
abstract class HomeModuleBinds {
    @Binds
    @PerActivity
    abstract fun bindContext(activity: MainActivity): Context
}

@Module
abstract class HomepageBuilder {
    @ContributesAndroidInjector(modules = [HomepageAssistedModule::class])
    abstract fun homepageFragment(): HomepageFragment

    @ContributesAndroidInjector(modules = [HomepageAssistedModule::class])
    abstract fun contentDetailFragment(): ContentDetailFragment
}

@Module(includes = [AssistedInject_HomepageAssistedModule::class])
@AssistedModule
interface HomepageAssistedModule