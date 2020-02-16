dependencies {
    implementation(AndroidX.Room.runtime)
    implementation(AndroidX.Room.ktx)
    kapt(AndroidX.Room.compiler)

    implementation("org.jsoup:jsoup:1.12.1")

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(AndroidX.Paging.common)
    implementation(AndroidX.Paging.runtime)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)
    implementation(AndroidX.Ktx.lifecycle)

    implementation(Dagger.dagger)
    kapt(Dagger.compiler)
    kapt(Dagger.androidProcessor)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Serialization.dependency)

    implementation(Timber.core)
    implementation(Gson.dependency)

    implementation(Retrofit.runtime)
    implementation(Retrofit.moshi)
    implementation(Retrofit.gson)

    implementation(OkHttp.core)
    implementation(OkHttp.loggingInterceptor)

    implementation(Moshi.kotlin)
    kapt(Moshi.compiler)

    testImplementation(AndroidX.Test.core)
    testImplementation(AndroidX.Test.junit)
    testImplementation(AndroidX.Test.rules)
    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    testImplementation(Testing.Mockito.kotlin)
    testImplementation(Testing.PowerMock.core)
    testImplementation(Testing.PowerMock.api)
    testImplementation(Testing.PowerMock.module)
    testImplementation(RoboElectric.dependency)

    implementation("org.threeten:threetenbp:1.3.7:no-tzdb")
}
