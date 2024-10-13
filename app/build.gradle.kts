plugins {
    id("com.kafka.compose")
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.kafka.user"
        versionCode = 88
        versionName = "0.48.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeCompiler {
        enableStrongSkippingMode = true
        includeSourceInformation = true
    }

    buildFeatures {
        compose = true
    }

    packaging {
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

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = System.getenv("RELEASE_SIGNING_PASSWORD")
            keyPassword = System.getenv("RELEASE_SIGNING_PASSWORD")
            keyAlias = System.getenv("RELEASE_SIGNING_ALIAS")
        }
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

    namespace = "com.kafka.user"

    lint {
        baseline = file("lint-baseline.xml")
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds = false
        // Ignore any tests
        ignoreTestSources = true
        // Make the build fail on any lint errors
        abortOnError = true
        checkDependencies = true
        warning += "AutoboxingStateCreation"
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.google.bom))

    implementation(projects.base.domain)
    implementation(projects.core.analytics)
    implementation(projects.core.downloader)
    implementation(projects.core.networking)
    implementation(projects.core.play)
    implementation(projects.corePlayback)
    implementation(projects.core.remoteConfig)
    implementation(projects.data.platform)
    implementation(projects.data.prefs)
    implementation(projects.data.repo)
    implementation(projects.domain)
    implementation(projects.navigation)
    implementation(projects.ui.auth)
    implementation(projects.ui.common)
    implementation(projects.ui.components)
    implementation(projects.ui.downloader)
    implementation(projects.ui.homepage)
    implementation(projects.ui.item.detail)
    implementation(projects.ui.library)
    implementation(projects.uiPlayback)
    implementation(projects.ui.profile)
    implementation(projects.ui.reader)
    implementation(projects.ui.search)
    implementation(projects.ui.shared)
    implementation(projects.ui.summary)
    implementation(projects.ui.theme)
    implementation(projects.ui.webview)

    implementation(compose.material3)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.coil.coil)
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material.material3)
    implementation(libs.compose.material.navigation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.util)
    implementation(libs.dataStore)
    implementation(libs.fetch)
    implementation(libs.firebase.firestore)
    implementation(libs.google.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.google.crashlytics)
    implementation(libs.google.messaging)
    implementation(libs.google.performance)
    implementation(libs.google.review)
    implementation(libs.google.appupdate)
    implementation(libs.haze)
    implementation(libs.haze.materials)
    implementation(libs.icons.feather)
    implementation(libs.icons.tabler)
    implementation(libs.kermit.crashlytics)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.stdlib)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.ktor.client.java)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization)

    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.okhttp.okhttp)
    implementation(libs.threeTenAbp)
    implementation(libs.profileinstaller)

    debugImplementation(libs.leakCanary)

    ksp(libs.kotlininject.compiler)
    implementation(libs.kotlininject.runtime)

    baselineProfile(projects.baselineprofile)
}

ksp {
//    arg("me.tatarka.inject.generateCompanionExtensions", "true")
    arg("me.tatarka.inject.enableJavaxAnnotations", "true")
//    arg("me.tatarka.inject.dumpGraph", "true")
}
