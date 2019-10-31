
plugins {
    id(Android.libPlugin)
    id(Kotlin.androidPlugin)
    id(Kotlin.kapt)
}

dependencies {
    implementation(AndroidX.Room.runtime)
    implementation(AndroidX.Room.rx)
    kapt(AndroidX.Room.compiler)

    implementation(Firebase.core)
    implementation(Firebase.firestore)

    implementation(PlayServices.basement)
    implementation(PlayServices.base)

//    implementation("com.google.firebase:firebase-auth:17.0.0")
//    implementation("com.google.android.gms:play-services-auth:16.0.1")

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(AndroidX.Paging.common)
    implementation(AndroidX.Paging.runtime)
    implementation(AndroidX.Paging.rx)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Coroutines.rx)
    implementation(KotlinX.Serialization.dependency)

    implementation(RxJava.rxJava2)
    implementation(RxJava.rxAndroid)
    implementation(RxJava.rxKotlin)

    implementation(Kodein.runtime)
    implementation(Kodein.androidX)

    implementation(Timber.core)
    implementation(Gson.dependency)

    implementation(Retrofit.runtime)
    implementation(Retrofit.moshi)
    implementation(Retrofit.rxjava)

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

    implementation("com.github.FrangSierra:RxFirebase:1.5.6")

    debugImplementation("com.amitshekhar.android:debug-db:1.0.5")
    debugImplementation("com.amitshekhar.android:debug-db-encrypt:1.0.5")
}
