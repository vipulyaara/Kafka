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
    api(platform(project(":data")))
    api(platform(project(":ui-common")))

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Timber.core)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.fragment)

    implementation(Libs.KotlinX.Coroutines.core)
    implementation(Libs.KotlinX.Coroutines.android)

    implementation(Libs.Coil.core)
    implementation(Libs.AndroidX.Ktx.fragment)

    implementation(Libs.Retrofit.runtime)
    implementation(Libs.Retrofit.moshi)
    implementation(Libs.OkHttp.core)
    implementation(Libs.OkHttp.loggingInterceptor)

    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    implementation("com.google.android.exoplayer:extension-mediasession:2.9.4")

    implementation(Libs.AndroidX.Ktx.core)

    implementation(Libs.material)
    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)
    implementation(Libs.Hilt.lifecycle)
    kapt(Libs.Hilt.lifecycle_compiler)
}
