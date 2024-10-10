plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.base.annotations)
                api(projects.core.networking)
                implementation(projects.data.models)
                implementation(projects.data.prefs)

                implementation(libs.firebase.auth)
                implementation(libs.ktor.client.core)

                implementation(libs.kotlininject.runtime)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)

            dependencies {
                implementation(projects.base.annotations)
                implementation(libs.javax.inject)
            }
        }

        val androidMain by getting {
            dependsOn(jvmCommon)

            dependencies {
            }
        }
    }
}

android {
    namespace = "com.kafka.data.platform"
}
