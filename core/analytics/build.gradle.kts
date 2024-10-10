plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.data.platform)

                implementation(libs.firebase.analytics)
                implementation(libs.kotlininject.runtime)
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
                implementation(projects.base.domain)
                implementation(projects.data.platform)

                implementation(project.dependencies.platform(libs.google.bom))
                implementation(libs.google.analytics)
                implementation(libs.google.crashlytics)
                implementation(libs.firebase.auth)

                implementation(libs.mixpanel)
            }
        }
    }
}

android {
    namespace = "com.kafka.analytics"
}
