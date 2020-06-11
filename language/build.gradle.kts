dependencies {
    implementation(project(Kafka.Data.nameDependency))
    implementation(project(Kafka.UiCompose.nameDependency))
    implementation(project(Kafka.UiCommon.nameDependency))

    implementation(Kotlin.stdlib)
    implementation(Jsoup.core)

    implementation(Store.core)

    implementation(AndroidX.appCompat)
    implementation(AndroidX.fragment)
    implementation(AndroidX.drawerLayout)
    implementation(AndroidX.material)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.workManager)
    implementation(AndroidX.viewPager2)
    implementation(AndroidX.palette)

    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)

    implementation(AndroidX.Room.runtime)
    kapt(AndroidX.Room.compiler)

    implementation(Retrofit.runtime)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    compileOnly(AssistedInject.annotationDagger2)
    kapt(AssistedInject.processorDagger2)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.fragment)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)
    implementation(AndroidX.Ktx.lifecycle)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)

    implementation(Lottie.core)

    implementation(Timber.core)

    implementation(Easeinterpolator.core)

    implementation(Coil.core)

    implementation(Stetho.core)
    implementation(Stetho.urlConnection)

    androidTestImplementation(AndroidX.annotation)
    androidTestImplementation(AndroidX.Test.junit)
    androidTestImplementation(AndroidX.Test.rules)
    androidTestImplementation(AndroidX.Espresso.core)
    androidTestImplementation(AndroidX.Espresso.intents)
    androidTestImplementation(AndroidX.Espresso.contrib)

    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    testImplementation(Testing.Mockito.kotlin)
    testImplementation(Testing.PowerMock.core)
    testImplementation(Testing.PowerMock.api)
    testImplementation(Testing.PowerMock.module)
}
