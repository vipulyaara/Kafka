import com.kafka.gradle.addKspDependencyForAllTargets
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.ksp)
}

kotlin {
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
                isStatic = true
                baseName = "KafkaKt"

                export(projects.ui.root)
                export(projects.core.analytics)
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
