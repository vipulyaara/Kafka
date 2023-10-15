plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

android {
    defaultConfig {
        applicationId = "com.kafka.user"
        versionCode = 44
        versionName = "0.5.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }

    packagingOptions {
        packagingOptions.resources.excludes += setOf(
            // Exclude AndroidX version files
            "META-INF/*.version",
            // Exclude consumer proguard files
            "META-INF/proguard/*",
            // Exclude the Firebase/Fabric/other random properties files
            "/*.properties",
            "fabric/*.properties",
            "META-INF/*.properties",
        )
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".debug"
        }

        release {
            signingConfig = signingConfigs["debug"]
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }

        create("rc") {
            initWith(buildTypes["release"])
            signingConfig = signingConfigs["debug"]
            matchingFallbacks += "release"
            versionNameSuffix = "-rc"
            applicationIdSuffix = ".rc"
        }

        create("benchmark") {
            initWith(buildTypes["release"])
            signingConfig = signingConfigs["debug"]
            matchingFallbacks += "release"
            proguardFiles("proguard-rules.pro")
        }
    }

    namespace  = "com.kafka.user"

    lint {
        baseline = file("lint-baseline.xml")
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds = false
        // Ignore any tests
        ignoreTestSources = true
        // Make the build fail on any lint errors
        abortOnError = true
        checkDependencies =  true
        warning += "AutoboxingStateCreation"
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.google.bom))

    implementation(projects.base.domain)
    implementation(projects.core.analytics)
    implementation(projects.core.downloader)
    implementation(projects.corePlayback)
    implementation(projects.core.recommendation)
    implementation(projects.core.remoteConfig)
    implementation(projects.data.repo)
    implementation(projects.domain)
    implementation(projects.navigation)
    implementation(projects.notifications)
    implementation(projects.ui.auth)
    implementation(projects.ui.common)
    implementation(projects.ui.components)
    implementation(projects.ui.downloader)
    implementation(projects.ui.homepage)
    implementation(projects.ui.item)
    implementation(projects.ui.library)
    implementation(projects.uiPlayback)
    implementation(projects.ui.reader)
    implementation(projects.ui.search)
    implementation(projects.ui.theme)
    implementation(projects.ui.webview)

    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.compose)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.coil.coil)
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.util)
    implementation(libs.dataStore)
    implementation(libs.fetch)
    implementation(libs.fetch.okhttp)
    implementation(libs.firestore.ktx)
    implementation(libs.google.analytics)
    implementation(libs.google.auth)
    implementation(libs.google.crashlytics)
    implementation(libs.google.dynamic.links)
    implementation(libs.google.firestore)
    implementation(libs.google.messaging)
    implementation(libs.google.performance)
    implementation(libs.google.review)
    implementation(libs.hilt.android)
    implementation(libs.icons.feather)
    implementation(libs.icons.tabler)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.stdlib)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.okhttp.okhttp)
    implementation(libs.retrofit.serialization)
    implementation(libs.threeTenAbp)
    implementation(libs.timber)
    implementation(libs.tracing)
    implementation(libs.profileinstaller)

    debugImplementation(libs.leakCanary)

    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.compiler)

    baselineProfile(projects.baselineprofile)
}
