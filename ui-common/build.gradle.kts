plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Publishing.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Publishing.minSdkVersion)
        targetSdkVersion(Publishing.compileSdkVersion)
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        versionCode = Publishing.versionCode
        versionName = Publishing.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(platform(project(":data")))
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Timber.core)

    implementation(Libs.KotlinX.Coroutines.core)
    implementation(Libs.KotlinX.Coroutines.android)

    implementation(Libs.Coil.core)
    implementation(Libs.ThreeTenBp.core)
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")

    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)

    implementation(Libs.material)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.palette)
    implementation(Libs.AndroidX.constraintLayout)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    implementation(Libs.AndroidX.Ktx.lifecycle)
    implementation(Libs.AndroidX.Ktx.liveData)
    implementation(Libs.AndroidX.Ktx.viewmodel)
    implementation(Libs.AndroidX.Ktx.fragment)

    implementation(Libs.Epoxy.core)
    implementation(Libs.Epoxy.databinding)
    kapt(Libs.Epoxy.processor)
}
