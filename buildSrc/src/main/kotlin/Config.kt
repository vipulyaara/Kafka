// Library version
object Kafka {
    const val publishVersion = "0.0.1"
    const val groupId = "com.kafka.user"

    const val compileSdkVersion = 28
    const val minSdkVersion = 21
}

// Core dependencies
object Kotlin {
    const val version = "1.3.50"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Kotlin.version}"
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
    const val version = "3.6.0-beta01"
    const val appPlugin = "com.android.application"
    const val libPlugin = "com.android.library"
    const val multiDex = "com.android.support:multidex:"
}

object AndroidX {
    const val fragment = "androidx.fragment:fragment:1.2.0-rc01"
    const val drawerLayout = "androidx.drawerlayout:drawerlayout:1.1.0-alpha02"
    const val annotation = "androidx.annotation:annotation:1.0.0"
    const val palette = "androidx.palette:palette:1.0.0-alpha1"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0-alpha01"
    const val appCompat = "androidx.appcompat:appcompat:1.1.0-beta01"
    const val leanback = "androidx.leanback:leanback:1.1.0-alpha02"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val material = "com.google.android.material:material:1.0.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-beta01"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:2.0.0-beta3"
    const val workManager = "android.arch.work:work-runtime:1.0.0-alpha13"

    object Navigation {
        private const val version = "2.2.0-alpha01"
        const val pluginVersion = "2.1.0-rc01"
        const val plugin = "androidx.navigation.safeargs"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        const val ui = "androidx.navigation:navigation-ui-ktx:$version"
    }

    object Ktx {
        const val sqliteVersion = "2.0.0"
        const val ktxVersion = "1.0.0"
        const val ktxLifecycleVersion = "2.2.0-alpha03"
        const val core = "androidx.core:core-ktx:$ktxVersion"
        const val fragment = "androidx.fragment:fragment-ktx:1.2.0-alpha02"
        const val palette = "androidx.palette:palette-ktx:$ktxVersion"
        const val sqlite = "androidx.sqlite:sqlite-ktx:$sqliteVersion"
        const val collection = "androidx.collection:collection-ktx:$ktxVersion"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$ktxLifecycleVersion"
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$ktxLifecycleVersion"

        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$ktxLifecycleVersion"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:$ktxLifecycleVersion"
        const val reactiveStreams =
            "androidx.lifecycle:lifecycle-reactivestreams-ktx:$ktxLifecycleVersion"
    }

    object Arch {
        const val version = "2.2.0-rc01"
        const val testingVersion = "2.0.0"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
        const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
        const val reactive_streams = "androidx.lifecycle:lifecycle-reactivestreams:$version"
        const val testing = "androidx.arch.core:core-testing:$testingVersion"
    }

    object Room {
        const val version = "2.2.0"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val rx = "androidx.room:room-rxjava2:$version"
        const val testing = "androidx.room:room-testing:$version"
        const val ktx = "androidx.room:room-ktx:$version"
    }

    object Paging {
        const val version = "2.1.0"
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
    const val version = "16.1.0"
    const val base = "com.google.android.gms:play-services-base:$version"
    const val basement = "com.google.android.gms:play-services-basement:$version"
    const val location = "com.google.android.gms:play-services-location:$version"


    const val pluginVersion = "4.2.0"
    const val plugin = "google-services"
}

object Dagger {
    private const val version = "2.24"
    const val dagger = "com.google.dagger:dagger:$version"
    const val androidSupport = "com.google.dagger:dagger-android-support:$version"
    const val compiler = "com.google.dagger:dagger-compiler:$version"
    const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
}

object AssistedInject {
    private const val version = "0.5.0"
    const val annotationDagger2 = "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
    const val processorDagger2 = "com.squareup.inject:assisted-inject-processor-dagger2:$version"
}

object Firebase {
    const val version = "16.0.7"
    const val core = "com.google.firebase:firebase-core:$version"
    const val firestore = "com.google.firebase:firebase-firestore:18.0.1"
}

object ExoPlayer {
    const val version = "2.9.6"
    const val player = "com.google.android.exoplayer:exoplayer:$version"
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
    const val version = "3.7.0"
    const val core = "com.airbnb.android:epoxy:$version"
    const val processor = "com.airbnb.android:epoxy-processor:$version"
    const val dataBinding = "com.airbnb.android:epoxy-databinding:$version"
    const val paging = "com.airbnb.android:epoxy-paging:$version"
    const val preloading = "com.airbnb.android:epoxy-glide-preloading:$version"
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
    const val version = "4.9.0"
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
        const val version = "1.3.2"
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
    const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
    const val compiler = "com.squareup.moshi:moshi-kotlin-codegen:$version"
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
