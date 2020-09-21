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
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Timber.core)

    implementation(Libs.KotlinX.Coroutines.core)
    implementation(Libs.KotlinX.Coroutines.android)

    implementation(Libs.Coil.core)

    implementation(Libs.Retrofit.runtime)
    implementation(Libs.Retrofit.moshi)
    implementation(Libs.OkHttp.core)
    implementation(Libs.OkHttp.loggingInterceptor)

    implementation(Libs.Moshi.kotlin)
    kapt(Libs.Moshi.compiler)

    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)
}
