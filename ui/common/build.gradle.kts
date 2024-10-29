plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                api(projects.core.imageLoading)
                implementation(projects.core.networking)
                api(projects.ui.theme)

                api(compose.animation)
                api(compose.foundation)
                api(compose.material3)
                api(compose.components.resources)
                api(compose.runtime)
                api(compose.ui)

                api(libs.jetbrains.adaptive)

                implementation(libs.icons.feather)
                implementation(libs.icons.font.awesome)
                implementation(libs.icons.tabler)
            }
        }

        androidMain {
            dependencies {
            }
        }
    }
}

android {
    namespace = "com.kafka.common"
}
