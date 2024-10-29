plugins {
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.common)
                implementation(projects.ui.components)

                implementation(compose.components.resources)
                implementation(compose.material3)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            }
        }
    }
}
