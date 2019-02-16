// Library version
object Kafka {
    const val publishVersion = "0.0.1"
    const val groupId = "com.airtel.kafka"

    const val compileSdkVersion = 28
    const val minSdkVersion = 19
}

// Core dependencies
object Kotlin {
    const val version = "1.3.20"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Kotlin.version}"
    const val plugin = "kotlin"
    const val kapt = "kotlin-kapt"
    const val androidPlugin = "kotlin-android"
    const val androidExtensionsPlugin = "kotlin-android-extensions"
}

object Result {
    const val version = "2.0.0"
    const val dependency = "com.github.kittinunf.result:result:$version"
}

object Json {
    const val version = "20170516"
    const val dependency = "org.json:json:$version"
}

object Android {
    const val version = "3.3.0"
    const val appPlugin = "com.android.application"
    const val libPlugin = "com.android.library"
    const val multiDex = "com.android.support:multidex:"
}

object AndroidX {
    const val supportVersion = "1.0.0"
    const val constraintLayoutVersion = "2.0.0-alpha2"
    const val workManagerVersion = "1.0.0-alpha13"
    const val annotation = "androidx.annotation:annotation:1.0.0"
    const val appCompat = "androidx.appcompat:appcompat:$supportVersion"
    const val leanback = "androidx.leanback:leanback:$supportVersion"
    const val cardView = "androidx.cardview:cardview:$supportVersion"
    const val material = "com.google.android.material:material:$supportVersion"
    const val recyclerView = "androidx.recyclerview:recyclerview:$supportVersion"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    const val workManager = "android.arch.work:work-runtime:$workManagerVersion"

    object Ktx {
        const val sqliteVersion = "2.0.0"
        const val ktxVersion = "1.0.0"
        const val ktxLifecycleVersion = "2.0.0-alpha1"
        const val core = "androidx.core:core-ktx:$ktxVersion"
        const val fragment = "androidx.fragment:fragment-ktx:$ktxVersion"
        const val palette = "androidx.palette:palette-ktx:$ktxVersion"
        const val sqlite = "androidx.sqlite:sqlite-ktx:$sqliteVersion"
        const val collection = "androidx.collection:collection-ktx:$ktxVersion"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$ktxLifecycleVersion"
        const val reactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams-ktx:$ktxLifecycleVersion"
    }

    object Arch {
        const val version = "2.0.0"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
        const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
        const val reactive_streams = "androidx.lifecycle:lifecycle-reactivestreams:$version"
        const val testing = "androidx.arch.core:core-testing:$version"
    }

    object Room {
        const val version = "2.1.0-alpha03"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val rx = "androidx.room:room-rxjava2:$version"
        const val testing = "androidx.room:room-testing:$version"
    }

    object Paging {
        const val version = "2.1.0-rc01"
        const val common = "androidx.paging:paging-common:$version"
        const val runtime = "androidx.paging:paging-runtime:$version"
        const val rx = "androidx.paging:paging-rxjava2:$version"
    }

    object Espresso {
        const val version = "3.1.0"
        const val core = "androidx.test.espresso:espresso-core:$version"
        const val intents = "androidx.test.espresso:espresso-intents:$version"
        const val contrib = "androidx.test.espresso:espresso-contrib:$version"
    }

    // Testing dependencies
    object Test {
        const val rulesVersion = "1.1.0"
        const val junitVersion = "1.0.0"
        const val rules = "androidx.test:rules:$rulesVersion"
        const val junit = "androidx.test.ext:junit:$junitVersion"
        const val core = "androidx.test:core:$junitVersion"
    }
}

object PlayServices {
    const val version = "16.0.0"
    const val base = "com.google.android.gms:play-services-base:$version"
    const val location = "com.google.android.gms:play-services-location:$version"
}

object Retrofit {
    const val version = "2.3.0"
    const val runtime = "com.squareup.retrofit2:retrofit:$version"
    const val gson = "com.squareup.retrofit2:converter-gson:$version"
    const val moshi = "com.squareup.retrofit2:converter-moshi:$version"
    const val rxjava = "com.squareup.retrofit2:adapter-rxjava2:$version"
}

object OkHttp {
    const val version = "3.6.0"
    const val core = "com.squareup.okhttp3:okhttp:$version"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
}

object Epoxy {
    const val version = "3.1.0"
    const val core = "com.airbnb.android:epoxy:$version"
    const val processor = "com.airbnb.android:epoxy-processor:$version"
    const val dataBinding = "com.airbnb.android:epoxy-databinding:$version"
    const val paging = "com.airbnb.android:epoxy-paging:$version"
}

object MvRx {
    const val version = "0.7.2"
    const val core = "com.airbnb.android:mvrx:$version"
}

// Modules dependencies
object Forge {
    const val version = "0.3.0"
    const val dependency = "com.github.kittinunf.forge:forge:$version"
}

object Gson {
    const val version = "2.8.5"
    const val dependency = "com.google.code.gson:gson:$version"
}

object Glide {
    const val version = "4.8.0"
    const val core = "com.github.bumptech.glide:glide:$version"
    const val compiler = "com.github.bumptech.glide:compiler:$version"
    const val transformations = "jp.wasabeef:glide-transformations:2.0.2"
}

object Timber {
    const val core = "com.jakewharton.timber:timber:4.5.1"
}

object ExpectAnim {
    const val core = "com.github.florent37:expectanim:1.0.7"
}

object Logger {
    const val core = "com.orhanobut:logger:2.2.0"
}

object SmartTabLayout {
    const val core = "com.ogaclejapan.smarttablayout:library:1.6.1@aar"
}

object Lottie {
    const val core = "com.airbnb.android:lottie:2.8.0"
}

object Easeinterpolator {
    const val core = "com.daasuu:EasingInterpolator:1.0.0"

}

object Jackson {
    const val version = "2.9.7"
    const val dependency = "com.fasterxml.jackson.module:jackson-module-kotlin:$version"
}

object KotlinX {
    object Coroutines {
        const val version = "1.1.1"
        val rx = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
        val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Serialization {
        const val version = "0.10.0"
        const val dependency = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$version"
        const val plugin = "kotlinx-serialization"
    }
}

object Testing {
    object PowerMock {
        const val version = "1.6.5"
        const val core = "org.powermock:powermock:$version"
        const val api = "org.powermock:powermock-api-mockito:$version"
        const val module = "org.powermock:powermock-module-junit4:$version"
    }

    object Mockito {
        const val core = "org.mockito:mockito-core:1.10.8"
        const val all = "org.mockito:mockito-all:1.10.19"
        const val kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC3"
    }
}

object Facebook {
    const val stetho = "com.facebook.stetho:stetho:1.5.0"
}

object Kodein {
    const val version = "5.2.0"
    const val runtime = "org.kodein.di:kodein-di-generic-jvm:$version"
    const val androidX = "org.kodein.di:kodein-di-framework-android-x:$version"
}

object Moshi {
    const val version = "1.8.0"
    const val dependency = "com.squareup.moshi:moshi:$version"
}

object Reactor {
    const val version = "3.2.2.RELEASE"
    const val core = "io.projectreactor:reactor-core:$version"
    const val test = "io.projectreactor:reactor-test:$version"
}

object RxJava {
    const val rxJava2 = "io.reactivex.rxjava2:rxjava:2.1.3"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.2"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"
}

// Lint
object Ktlint {
    const val version = "1.20.1"
    const val plugin = "org.jmailen.kotlinter"
}

// Testing dependencies
object JUnit {
    const val version = "4.12"
    const val dependency = "junit:junit:$version"
}

object MockServer {
    const val version = "5.4.1"
    const val dependency = "org.mock-server:mockserver-netty:$version"
}

object Jacoco {
    const val version = "0.8.3"
    const val plugin = "jacoco"

    object Android {
        const val version = "0.1.3"
        const val plugin = "jacoco-android"
    }
}

object RoboElectric {
    const val version = "3.8"
    const val dependency = "org.robolectric:robolectric:$version"
}

object Release {
    object MavenPublish {
        const val plugin = "maven-publish"
    }

    object Bintray {
        const val version = "1.8.4"
        const val plugin = "com.jfrog.bintray"
    }
}

object Stetho {
    const val version = "1.5.0"
    const val core = "com.facebook.stetho:stetho:$version"
    const val urlConnection = "com.facebook.stetho:stetho-urlconnection:$version"
}
