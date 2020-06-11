dependencies {
    implementation(Jsoup.core)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(AndroidX.Room.runtime)
    implementation(AndroidX.Room.ktx)
    kapt(AndroidX.Room.compiler)

    implementation(Store.core)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)
    implementation(AndroidX.Ktx.lifecycle)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Serialization.dependency)

    implementation(ThreeTenBp.core)

    implementation(Timber.core)

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
    testImplementation(KotlinX.Coroutines.test)
    testImplementation(KotlinX.Coroutines.android)

    testImplementation(Mockk.core)

    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    kaptTest(AndroidX.Room.compiler)

    kaptTest(Dagger.compiler)
}
