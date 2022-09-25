plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Publishing.compileSdk)

    defaultConfig {
        minSdkVersion(Publishing.minSdk)
        targetSdkVersion(Publishing.compileSdk)
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

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
        exclude("META-INF/*")
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api platform(project(":data"))

    api libs.androidx.core
            api libs.androidx.collection

            implementation libs.jsoup
            api libs.threeTenAbp

            api libs.dataStore

            api libs.hilt.library
            kapt libs.hilt.compiler

            api libs.kotlin.serialization
            api libs.kotlin.coroutines.core

            api libs.kotlin.stdlib

    implementation libs.coil.core

    implementation libs.ExoPlayer.player
    implementation "com.google.android.exoplayer:extension-mediasession:2.9.4"
}
