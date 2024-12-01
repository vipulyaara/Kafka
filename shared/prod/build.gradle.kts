import com.kafka.gradle.addKspDependencyForAllTargets
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.ksp)
    kotlin("native.cocoapods")
}

kotlin {

    cocoapods {
        summary = "Shared module for production"
        homepage = "https://www.getkafka.app"
        version = "1.0.0"
        ios.deploymentTarget = "14.1"
        name = "shared"

        framework {
            baseName = "shared"
            isStatic = true
            linkerOpts.add("-lsqlite3")
            linkerOpts.add("-rpath")
            linkerOpts.add("/usr/lib/swift")
            freeCompilerArgs += listOf(
                "-linker-options", "-lsqlite3",
                "-linker-options", "-rpath",
                "-linker-options", "/usr/lib/swift"
            )

            export(projects.base.domain)
            export(projects.core.analytics)
            export(projects.core.downloader)
            export(projects.core.networking)
            export(projects.core.play)
            export(projects.corePlayback)
            export(projects.core.reader)
            export(projects.core.remoteConfig)
            export(projects.data.platform)
            export(projects.data.repo)
            export(projects.domain)
            export(projects.navigation)

            export(projects.ui.auth)
            export(projects.ui.common)
            export(projects.ui.components)
            export(projects.ui.homepage)
            export(projects.ui.item.detail)
            export(projects.ui.library)
            export(projects.uiPlayback)
            export(projects.ui.profile)
            export(projects.ui.reader.epub)
            export(projects.ui.root)
            export(projects.ui.search)
            export(projects.ui.summary)
            export(projects.ui.theme)
            export(projects.ui.webview)

            export(libs.jetbrains.navigation.compose)

            linkerOpts += listOf(
                "-framework", "FirebaseCore",
                "-framework", "FirebaseAnalytics",
                "-framework", "FirebaseFirestore",
                "-framework", "Foundation",
                "-framework", "UIKit"
            )

            export(libs.kotlin.serialization)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.common)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.kotlininject.runtime)
            }
        }

        jvmMain {
            dependsOn(jvmCommon)
        }

        iosMain {
        }

        androidMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.okhttp.okhttp)
            }
        }

        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.framework {
                baseName = "shared"
                isStatic = true

                export(projects.base.domain)
                export(projects.core.analytics)
                export(projects.core.downloader)
                export(projects.core.networking)
                export(projects.core.play)
                export(projects.corePlayback)
                export(projects.core.reader)
                export(projects.core.remoteConfig)
                export(projects.data.platform)
                export(projects.data.repo)
                export(projects.domain)
                export(projects.navigation)

                export(projects.ui.auth)
                export(projects.ui.common)
                export(projects.ui.components)
                export(projects.ui.homepage)
                export(projects.ui.item.detail)
                export(projects.ui.library)
                export(projects.uiPlayback)
                export(projects.ui.profile)
                export(projects.ui.reader.epub)
                export(projects.ui.root)
                export(projects.ui.search)
                export(projects.ui.summary)
                export(projects.ui.theme)
                export(projects.ui.webview)

                export(libs.jetbrains.navigation.compose)
            }
        }
    }
}

android {
    namespace = "com.kafka.shared"
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
