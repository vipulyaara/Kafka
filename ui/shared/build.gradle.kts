import com.kafka.gradle.addKspDependencyForAllTargets
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.ksp)
}

buildConfig {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildConfigField(
        "String",
        "GOOGLE_SERVER_CLIENT_ID",
        properties["GOOGLE_SERVER_CLIENT_ID"]?.toString()
            ?: System.getenv("GOOGLE_SERVER_CLIENT_ID")
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.downloader)
                implementation(projects.core.networking)
                implementation(projects.core.play)
                implementation(projects.corePlayback)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.platform)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.common)
                implementation(projects.ui.components)
                implementation(projects.ui.downloader)
                implementation(projects.ui.homepage)
                implementation(projects.uiPlayback)

                implementation(libs.firebase.firestore)
                implementation(libs.kotlin.coroutines.swing)
                implementation(libs.kotlininject.runtime)
                implementation(libs.threeTenAbp)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)
        }

        val androidMain by getting {
            dependsOn(jvmCommon)

            dependencies {
                implementation(libs.firebase.common)
                implementation(libs.kermit)
                implementation(libs.kermit.crashlytics)
                implementation(libs.threeTenAbp)
            }
        }
    }
}

android {
    namespace = "com.kafka.shared"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
