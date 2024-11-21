plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)

                implementation(libs.firebase.remote.config)
                implementation(libs.kotlin.serialization)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommon)
            dependencies {
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
    namespace = "com.kafka.remote.comfig"
}
