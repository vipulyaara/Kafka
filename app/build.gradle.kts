plugins {
    id("com.android.application")
    kotlin("android")
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Libs.Kotlin.version
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

    compileOptions {
        // Flag to enable support for the new language APIs
//        coreLibraryDesugaringEnabled = true
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
        exclude("META-INF/ui-graphics_release.kotlin_module/")
        exclude("META-INF/*")
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
    implementation(Libs.Hilt.workManager)

    implementation(Libs.Retrofit.runtime)

    implementation(Libs.material)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.Ktx.workManager)
    implementation(Libs.AndroidX.palette)
    implementation(Libs.AndroidX.appStartup)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.lifecycle_process)

    implementation("androidx.core:core:1.5.0-alpha04")
    implementation(Libs.AndroidX.Compose.runtime)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.layout)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.Accompanist.coil)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

//    implementation(Libs.Lottie.core)
    implementation(Libs.Epoxy.core)
//    implementation(Libs.Epoxy.databinding)
//    kapt(Libs.Epoxy.processor)

//    coreLibraryDesugaring Libs.jdkDesugar
}

apply(plugin = "com.google.gms.google-services")
