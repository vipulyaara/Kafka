plugins {
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.ui.components)

                implementation(compose.components.resources)
                implementation(compose.material3)

                implementation(libs.webview)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            }
        }
    }
}
