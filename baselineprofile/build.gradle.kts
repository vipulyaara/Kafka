import com.android.build.api.dsl.ManagedVirtualDevice

@Suppress("DSL_SCOPE_VIOLATION") // Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.kafka.baseline.profile"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6Api31") {
            device = "Pixel 6"
            apiLevel = 31
            systemImageSource = "aosp"
        }
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    managedDevices += "pixel6Api31"
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.benchmark.macro.junit4)
    implementation(libs.androidx.rules)
}
