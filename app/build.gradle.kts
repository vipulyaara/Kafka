plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Publishing.compileSdkVersion)

    defaultConfig {
        applicationId = Publishing.applicationId

        minSdkVersion(Publishing.minSdkVersion)
        targetSdkVersion(Publishing.compileSdkVersion)
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        versionCode = Publishing.versionCode
        versionName = Publishing.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dataBinding.isEnabled = true

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    packagingOptions {
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(platform(project(":player")))
    api(platform(project(":ui-common")))
    api(platform(project(":content")))
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Timber.core)

    implementation(Libs.KotlinX.Coroutines.core)
    implementation(Libs.KotlinX.Coroutines.android)

    implementation(Libs.ExoPlayer.player)

    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.dynamicLinks)

    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)
    implementation(Libs.Hilt.lifecycle)
    kapt(Libs.Hilt.lifecycle_compiler)

    implementation(Libs.Retrofit.runtime)
    implementation(Libs.Retrofit.moshi)
    implementation(Libs.OkHttp.core)
    implementation(Libs.OkHttp.loggingInterceptor)

    implementation(Libs.material)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.workManager)
    implementation(Libs.AndroidX.palette)
    implementation(Libs.AndroidX.appStartup)
    implementation(Libs.AndroidX.constraintLayout)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    implementation(Libs.Lottie.core)
    implementation(Libs.Epoxy.core)
    implementation(Libs.Epoxy.databinding)
    kapt(Libs.Epoxy.processor)
}

apply(plugin = "com.google.gms.google-services")
