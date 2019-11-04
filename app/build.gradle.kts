
import com.android.build.gradle.BaseExtension

plugins {
    id(Android.appPlugin)
    id(Kotlin.androidPlugin)
    id(Kotlin.kapt)
    id(Kotlin.androidExtensionsPlugin)
}

//androidExtensions {
//    configure(delegateClosureOf<AndroidExtensionsExtension> {
//        isExperimental = true
//    })
//}

dependencies {
    implementation(project(":data"))
    implementation(project(":player"))

    implementation(Kotlin.stdlib)

    implementation("com.google.guava:guava:26.0-android")

    implementation(PlayServices.basement)
    implementation(PlayServices.base)

    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.cardView)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.workManager)

    implementation(Dagger.dagger)
    implementation(Dagger.androidSupport)
    kapt(Dagger.compiler)
    kapt(Dagger.androidProcessor)

    compileOnly(AssistedInject.annotationDagger2)
    kapt(AssistedInject.processorDagger2)

    implementation(AndroidX.Room.runtime)
    kapt(AndroidX.Room.compiler)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)
//    implementation(AndroidX.Navigation.safeArgs)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.fragment)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)
    implementation(AndroidX.Ktx.runtime)
    implementation(AndroidX.Ktx.liveData)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Serialization.dependency)

    implementation(Epoxy.core)
    implementation(Epoxy.dataBinding)
    kapt(Epoxy.processor)
    implementation(Epoxy.paging)
    implementation(Epoxy.preloading)

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
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        this.isEnabled = true
    }

    kapt {
        javacOptions {
            // Increase the max count of errors from annotation processors.
            // Default is 100.
            option("-Xmaxerrs", 500)
        }
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
