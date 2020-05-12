// Library version
object Kafka {
    const val name = "app"
    const val applicationId = "com.kafka.android"
    const val versionName = "0.0.1"
    const val versionCode = 1

    const val compileSdkVersion = 29
    const val minSdkVersion = 23

    object BaseData {
        const val name = "data-base"
        const val nameDependency = ":$name"
    }

    object Data {
        const val name = "data"
        const val nameDependency = ":$name"
    }

    object Domain {
        const val name = "domain"
        const val nameDependency = ":$name"
    }

    object Reader {
        const val name = "reader"
        const val nameDependency = ":$name"
    }

    object Player {
        const val name = "player"
        const val nameDependency = ":$name"
    }

    object Language {
        const val name = "language"
        const val nameDependency = ":$name"
    }

    object Search {
        const val name = "search"
        const val nameDependency = ":$name"
    }

    object Logger {
        const val name = "logger"
        const val nameDependency = ":$name"
    }

    object UiCommon {
        const val name = "ui-common"
        const val nameDependency = ":$name"
    }

    object UiCompose {
        const val name = "ui-compose"
        const val nameDependency = ":$name"
    }
}

object FolioReader{
    val core = "com.folioreader:folioreader:0.5.4"
}

object ExoPlayer {
    private const val version = "2.9.6"
    const val player = "com.google.android.exoplayer:exoplayer:$version"
}

object FinestWebView {
    const val core = "com.thefinestartist:finestwebview:1.2.7"
}

// Core dependencies
object Kotlin {
    const val version = "1.3.71"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    const val plugin = "kotlin"
    const val kapt = "kotlin-kapt"
    const val androidPlugin = "kotlin-android"
    const val androidExtensionsPlugin = "kotlin-android-extensions"
}

object Store {
    private const val version = "4.0.0-alpha03"
    const val core = "com.dropbox.mobile.store:store4:$version"
}

object Coil {
    private const val version = "0.9.2"
    const val core = "io.coil-kt:coil:$version"
}

object Freshchat {
    private const val version = "3.0.0"
    const val core = "com.github.freshdesk:freshchat-android:$version"
}

object Sendbird {
    private const val version = "3.0.90"
    const val core = "com.sendbird.sdk:sendbird-android-sdk:$version"
}

object Jsoup {
    private const val version = "1.12.1"
    const val core = "org.jsoup:jsoup:$version"
}

object ThreeTenBp {
    private const val version = "1.0.5"
    const val core = "com.jakewharton.threetenabp:threetenabp:$version"
}

object Android {
    private const val version = "4.1.0-alpha08"
    const val appPlugin = "com.android.application"
    const val libPlugin = "com.android.library"
    const val gradlePlugin = "com.android.tools.build:gradle:$version"
    const val multiDex = "com.android.support:multidex:"
}

object AndroidX {
    const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
    const val fragment = "androidx.fragment:fragment:1.3.0-alpha02"
    const val drawerLayout = "androidx.drawerlayout:drawerlayout:1.1.0-alpha02"
    const val annotation = "androidx.annotation:annotation:1.0.0"
    const val palette = "androidx.palette:palette:1.0.0-alpha1"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0-rc01"
    const val appCompat = "androidx.appcompat:appcompat:1.2.0-alpha03"
    const val leanback = "androidx.leanback:leanback:1.1.0-alpha02"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val material = "com.google.android.material:material:1.2.0-alpha01"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-beta01"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:2.0.0-beta3"
    const val workManager = "android.arch.work:work-runtime:1.0.0-alpha13"
    const val preference = "androidx.preference:preference:1.1.0"

    object Navigation {
        private const val version = "2.2.0-alpha01"
        const val pluginVersion = "2.1.0"
        const val plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        const val ui = "androidx.navigation:navigation-ui-ktx:$version"
    }

    object Compose {
        private const val version = "0.1.0-dev10"

        const val extensions = "dev.chrisbanes.accompanist:accompanist-mdc-theme:0.1.0-SNAPSHOT"
        const val runtime = "androidx.compose:compose-runtime:$version"
        const val framework = "androidx.ui:ui-framework:$version"
        const val layout = "androidx.ui:ui-layout:$version"
        const val material = "androidx.ui:ui-material:$version"
        const val foundation = "androidx.ui:ui-foundation:$version"
        const val animation = "androidx.ui:ui-animation:$version"
        const val tooling = "androidx.ui:ui-tooling:$version"
    }

    object Ktx {
        private const val sqliteVersion = "2.0.0"
        private const val ktxVersion = "1.0.0"
        private const val ktxLifecycleVersion = "2.2.0"
        private const val workManagerVersion = "2.3.2"
        const val core = "androidx.core:core-ktx:$ktxVersion"
        const val fragment = "androidx.fragment:fragment-ktx:1.2.0-alpha02"
        const val palette = "androidx.palette:palette-ktx:$ktxVersion"
        const val sqlite = "androidx.sqlite:sqlite-ktx:$sqliteVersion"
        const val collection = "androidx.collection:collection-ktx:$ktxVersion"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$ktxLifecycleVersion"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:$ktxLifecycleVersion"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$ktxLifecycleVersion"
        const val reactiveStreams =
            "androidx.lifecycle:lifecycle-reactivestreams-ktx:$ktxLifecycleVersion"
        const val workManager = "androidx.work:work-runtime-ktx:$workManagerVersion"
    }

    object Arch {
        private const val version = "2.2.0"
        private const val testingVersion = "2.0.0"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
        const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
        const val reactive_streams = "androidx.lifecycle:lifecycle-reactivestreams:$version"
        const val testing = "androidx.arch.core:core-testing:$testingVersion"
    }

    object Room {
        private const val version = "2.2.0"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val rx = "androidx.room:room-rxjava2:$version"
        const val testing = "androidx.room:room-testing:$version"
        const val ktx = "androidx.room:room-ktx:$version"
    }

    object Paging {
        private const val version = "2.1.0"
        const val common = "androidx.paging:paging-common:$version"
        const val runtime = "androidx.paging:paging-runtime:$version"
        const val rx = "androidx.paging:paging-rxjava2:$version"
    }

    object Espresso {
        private const val version = "3.1.0"
        const val core = "androidx.test.espresso:espresso-core:$version"
        const val intents = "androidx.test.espresso:espresso-intents:$version"
        const val contrib = "androidx.test.espresso:espresso-contrib:$version"
    }

    // Testing dependencies
    object Test {
        private const val rulesVersion = "1.1.0"
        private const val junitVersion = "1.0.0"
        const val rules = "androidx.test:rules:$rulesVersion"
        const val junit = "androidx.test.ext:junit:$junitVersion"
        const val core = "androidx.test:core:$junitVersion"
    }
}

object Mockk {
    const val core = "io.mockk:mockk:1.9.3"
}

object Hunter {
    private const val debugVersion = "0.9.6"
    const val transformVersion = "0.9.3"
    const val debugPlugin = "hunter-debug"
    const val transformPlugin = "hunter-transform"
    const val debug = "com.quinn.hunter:hunter-debug-library:$debugVersion"
}

object GooglePlayServices {
    private const val version = "17.0.0"
    const val base = "com.google.android.gms:play-services-base:$version"
    const val basement = "com.google.android.gms:play-services-basement:$version"
    const val location = "com.google.android.gms:play-services-location:$version"

    const val pluginVersion = "4.2.0"
    const val libPlugin = "com.google.gms.google-services"
}

object Firebase {
    const val firestore = "com.google.firebase:firebase-firestore:18.0.1"
    const val analytics = "com.google.firebase:firebase-analytics:17.2.2"
    const val messaging = "com.google.firebase:firebase-messaging:20.1.4"
    const val perf = "com.google.firebase:firebase-perf:19.0.0"
    const val perfPlugin = "perf-plugin"
    const val perfPluginVersion = "1.3.1"
}

object Mapbox {
    private const val version = "9.0.0"
    const val core = "com.mapbox.mapboxsdk:mapbox-android-sdk:$version"
}

object Retrofit {
    private const val version = "2.7.1"
    const val runtime = "com.squareup.retrofit2:retrofit:$version"
    const val gson = "com.squareup.retrofit2:converter-gson:$version"
    const val moshi = "com.squareup.retrofit2:converter-moshi:$version"
    const val rxjava = "com.squareup.retrofit2:adapter-rxjava2:$version"
}

object OkHttp {
    private const val version = "4.4.0"
    const val core = "com.squareup.okhttp3:okhttp:$version"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
}

object Hugo {
    const val version = "1.2.1"
    const val plugin = "com.jakewharton.hugo"
}

object Dagger {
    private const val version = "2.27"
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

object Glide {
    private const val version = "4.9.0"
    const val core = "com.github.bumptech.glide:glide:$version"
    const val compiler = "com.github.bumptech.glide:compiler:$version"
    const val transformations = "jp.wasabeef:glide-transformations:2.0.2"
}

object Timber {
    const val core = "com.jakewharton.timber:timber:4.5.1"
}

object Logger {
    const val core = "com.orhanobut:logger:2.2.0"
}

object SmartTabLayout {
    const val core = "com.ogaclejapan.smarttablayout:library:2.0.0@aar"
}

object Lottie {
    const val core = "com.airbnb.android:lottie:2.8.0"
}

object Easeinterpolator {
    const val core = "com.daasuu:EasingInterpolator:1.0.0"
}

object KotlinX {
    object Coroutines {
        private const val version = "1.3.2"
        val rx = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
        val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Serialization {
        private const val version = "0.20.0"
        const val dependency = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$version"
        const val plugin = "kotlinx-serialization"
    }
}

object Testing {
    object PowerMock {
        private const val version = "1.6.5"
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

object Moshi {
    private const val version = "1.8.0"
    const val dependency = "com.squareup.moshi:moshi:$version"
    const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
    const val compiler = "com.squareup.moshi:moshi-kotlin-codegen:$version"
}

// Lint
object Ktlint {
    const val version = "1.20.1"
    const val plugin = "org.jmailen.kotlinter"
}

// Testing dependencies
object JUnit {
    private const val version = "4.12"
    const val dependency = "junit:junit:$version"
}

object MockServer {
    private const val version = "5.4.1"
    const val dependency = "org.mock-server:mockserver-netty:$version"
}

object Jacoco {
    const val version = "0.8.3"
    const val plugin = "jacoco"

    object Android {
        const val version = "0.1.4"
        const val plugin = "jacoco-android"
    }
}

object RoboElectric {
    private const val version = "3.8"
    const val dependency = "org.robolectric:robolectric:$version"
}

object LeakCanary {
    private const val version = "2.0-beta-3"
    const val core = "com.squareup.leakcanary:leakcanary-android:$version"
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
    private const val version = "1.5.0"
    const val core = "com.facebook.stetho:stetho:$version"
    const val urlConnection = "com.facebook.stetho:stetho-urlconnection:$version"
}
