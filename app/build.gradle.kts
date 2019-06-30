import com.android.build.gradle.BaseExtension
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id(Android.appPlugin)
    id(Kotlin.androidPlugin)
    id(Kotlin.kapt)
    id(Kotlin.androidExtensionsPlugin)
}

androidExtensions {
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

dependencies {
    implementation(project(":data"))
    implementation(project(":player"))

    implementation(Kotlin.stdlib)

    implementation("com.google.guava:guava:26.0-android")
    implementation("com.github.igalata:Bubble-Picker:v0.2.4")

    implementation(PlayServices.basement)
    implementation(PlayServices.base)

    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.cardView)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.workManager)
    implementation(AndroidX.Paging.common)
    implementation(AndroidX.Paging.runtime)
    implementation(AndroidX.Paging.rx)

    implementation(AndroidX.Room.runtime)
    implementation(AndroidX.Room.rx)
    kapt(AndroidX.Room.compiler)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.fragment)
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

    implementation(Epoxy.core)
    implementation(Epoxy.dataBinding)
    kapt(Epoxy.processor)
    implementation(Epoxy.paging)

    implementation(MvRx.core)

    implementation(Lottie.core)

    implementation(Kodein.runtime)
    implementation(Kodein.androidX)

    implementation(Timber.core)

    implementation(Easeinterpolator.core)

    implementation(Android.multiDex)

    implementation(Glide.core)
    kapt(Glide.compiler)
    implementation(Glide.transformations)

    implementation(ExpectAnim.core)

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

    implementation("com.github.zomato:androidphotofilters:1.0.2")
}

configure<BaseExtension> {
    compileSdkVersion(Kafka.compileSdkVersion)

    defaultConfig {
        applicationId = "com.kafka.user"
        minSdkVersion(Kafka.minSdkVersion)
        targetSdkVersion(Kafka.compileSdkVersion)
        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    dataBinding {
        this.isEnabled = true
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/rxkotlin.properties")
    }
}
apply(plugin = "com.google.gms.google-services")
