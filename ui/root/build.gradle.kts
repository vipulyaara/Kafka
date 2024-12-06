plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

android {
    namespace = "com.kafka.root"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.domain)
                implementation(projects.ui.components)
                implementation(projects.navigation)
                implementation(projects.uiPlayback)

                implementation(projects.ui.auth)
                implementation(projects.ui.common)
                implementation(projects.ui.components)
                implementation(projects.ui.homepage)
                implementation(projects.ui.item.detail)
                implementation(projects.ui.item.reviews)
                implementation(projects.ui.library)
                implementation(projects.uiPlayback)
                implementation(projects.ui.profile)
                implementation(projects.ui.reader.epub)
                implementation(projects.ui.search)
                implementation(projects.ui.summary)
                implementation(projects.ui.theme)
                implementation(projects.ui.webview)

                implementation(compose.components.resources)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)

                implementation(libs.haze.materials)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
                implementation(libs.jetbrains.navigation.compose)
                implementation(libs.jetbrains.material.navigation)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.kotlin.coroutines.swing)
            }
        }
        
        androidMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.accompanist.permissions)
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}
