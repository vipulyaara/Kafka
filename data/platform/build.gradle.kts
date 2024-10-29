plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.base.annotations)
                api(projects.core.networking)
                implementation(projects.data.models)
                implementation(projects.data.prefs)

                implementation(libs.kotlininject.runtime)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(projects.base.annotations)
            }
        }

        androidMain {
            dependsOn(jvmCommon)
            dependencies {
            }
        }
    }
}

android {
    namespace = "com.kafka.data.platform"
}
