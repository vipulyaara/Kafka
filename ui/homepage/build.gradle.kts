plugins {
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.components)
                implementation(projects.uiPlayback)

                implementation(compose.components.resources)
                implementation(compose.material3)

                implementation(libs.haze)
                implementation(libs.material.kolor)
                implementation(libs.haze.materials)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            }
        }
    }
}
