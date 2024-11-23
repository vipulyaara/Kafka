plugins {
    id("com.kafka.compose")
    id("com.kafka.android.application")
    alias(libs.plugins.androidx.baselineprofile)
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
        versionCode = 1
        versionName = "0.0.1"
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

androidComponents {
    onVariants(selector().withBuildType("release")) {
        it.packaging.resources.excludes.addAll(
            setOf(
                // Exclude AndroidX version files
                "META-INF/*.version",
                // Exclude consumer proguard files
                "META-INF/proguard/*",
                // Exclude the Firebase/Fabric/other random properties files
                "/*.properties",
                "fabric/*.properties",
                "META-INF/*.properties",
                "META-INF/kotlinx-io.kotlin_module",
                "META-INF/atomicfu.kotlin_module",
                "META-INF/kotlinx-coroutines-io.kotlin_module",
                "META-INF/kotlinx-coroutines-core.kotlin_module"
            )
        )
    }
}

dependencies {
    implementation(platform(libs.google.bom))

    implementation(projects.shared.prod)

    implementation(compose.material3)
    implementation(libs.compose.material.navigation)

    implementation(libs.androidx.activity.compose)

    implementation(libs.filekit)
    implementation(libs.google.messaging)

    implementation(project.dependencies.platform(libs.openai.kotlin.bom))
    implementation(libs.openai.client)

    implementation(libs.ktor.client.contentnegotiation)

    implementation(libs.profileinstaller)
    debugImplementation(libs.leakCanary)
    baselineProfile(projects.baselineprofile)

    ksp(libs.kotlininject.compiler)
    implementation(libs.kotlininject.runtime)
}
