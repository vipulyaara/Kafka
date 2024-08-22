import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.kafka.user"
        versionCode = 76
        versionName = "0.36.0"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField(
            "String",
            "GOOGLE_SERVER_CLIENT_ID",
            properties["GOOGLE_SERVER_CLIENT_ID"]?.toString()
                ?: System.getenv("GOOGLE_SERVER_CLIENT_ID")
        )
        buildConfigField(
            "String",
            "PIPELESS_AUTH_TOKEN",
            properties["PIPELESS_AUTH_TOKEN"]?.toString() ?: System.getenv("PIPELESS_AUTH_TOKEN")
        )
        buildConfigField(
            "String",
            "OPEN_AI_API_KEY",
            properties["OPEN_AI_API_KEY"]?.toString() ?: System.getenv("OPEN_AI_API_KEY")
        )
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
    implementation(projects.core.play)
    implementation(projects.corePlayback)
    implementation(projects.core.remoteConfig)
    implementation(projects.data.prefs)
    implementation(projects.data.repo)
    implementation(projects.domain)
    implementation(projects.navigation)
    implementation(projects.ui.auth)
    implementation(projects.ui.common)
    implementation(projects.ui.components)
    implementation(projects.ui.downloader)
    implementation(projects.ui.homepage)
    implementation(projects.ui.item)
    implementation(projects.ui.library)
    implementation(projects.uiPlayback)
    implementation(projects.ui.profile)
    implementation(projects.ui.reader)
    implementation(projects.ui.search)
    implementation(projects.ui.summary)
    implementation(projects.ui.theme)
    implementation(projects.ui.webview)

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
    implementation(libs.firestore.ktx)
    implementation(libs.google.analytics)
    implementation(libs.google.auth)
    implementation(libs.google.crashlytics)
    implementation(libs.google.firestore)
    implementation(libs.google.messaging)
    implementation(libs.google.performance)
    implementation(libs.google.review)
    implementation(libs.google.appupdate)
    implementation(libs.hilt.android)
    implementation(libs.icons.feather)
    implementation(libs.icons.tabler)
    implementation(libs.kermit.crashlytics)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.stdlib)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.okhttp.okhttp)
    implementation(libs.retrofit.serialization)
    implementation(libs.threeTenAbp)
    implementation(libs.profileinstaller)

    debugImplementation(libs.leakCanary)

    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.compiler)

    baselineProfile(projects.baselineprofile)
}
