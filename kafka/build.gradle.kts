
plugins {
    id(Android.libPlugin)
    id(Kotlin.androidPlugin)
    id(Kotlin.kapt)
}

dependencies {
    api(project(":data"))
    implementation(AndroidX.Room.runtime)
    implementation(AndroidX.Room.rx)
    kapt(AndroidX.Room.compiler)
    testImplementation(AndroidX.Room.testing)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)
    testImplementation(AndroidX.Arch.testing)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Coroutines.rx)

    implementation(RxJava.rxJava2)
    implementation(RxJava.rxAndroid)
    implementation(RxJava.rxKotlin)

    implementation(Kodein.runtime)
    implementation(Kodein.androidX)

    implementation(Timber.core)
    implementation(Gson.dependency)

    implementation(Moshi.dependency)

    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    testImplementation(Testing.Mockito.kotlin)
    testImplementation(Testing.PowerMock.core)
    testImplementation(Testing.PowerMock.api)
    testImplementation(Testing.PowerMock.module)
}
