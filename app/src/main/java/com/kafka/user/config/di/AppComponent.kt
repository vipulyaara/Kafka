package com.kafka.user.config.di

import com.kafka.data.data.config.DataModule
import com.kafka.data.data.config.DatabaseModule
import com.kafka.data.data.config.ServiceModule
import com.kafka.user.KafkaApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class,
        AppModule::class,
        DataModule::class,
        ServiceModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent : AndroidInjector<KafkaApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: KafkaApplication): AppComponent
    }
}
