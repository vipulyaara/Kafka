//package com.kafka.user.injection
//
//import androidx.lifecycle.ViewModelProvider
//import com.kafka.user.common.KafkaViewModelFactory
//import dagger.Binds
//import dagger.Module
//
///**
// * Module used to define the connection between the framework's [ViewModelProvider.Factory] and
// * our own implementation: [KafkaViewModelFactory].
// */
//@Module
//@Suppress("UNUSED")
//abstract class ViewModelModule {
//
//    @Binds
//    internal abstract fun bindViewModelFactory(factory: KafkaViewModelFactory):
//        ViewModelProvider.Factory
//}
