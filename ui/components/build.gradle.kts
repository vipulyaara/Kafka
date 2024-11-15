plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.data.repo)
                implementation(projects.navigation)
                api(projects.ui.common)

                api(compose.components.resources)
                api(compose.components.uiToolingPreview)

                api(compose.animation)
                api(compose.foundation)
                api(compose.material3)
                api(compose.runtime)
                api(compose.ui)

                implementation(libs.haze)
                implementation(libs.haze.materials)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.lottie.compose)
                implementation(libs.lottie.core)
            }
        }
    }
}

android {
    namespace = "com.kafka.ui.components"
}
