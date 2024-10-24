plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.data.platform)

                implementation(libs.kotlininject.runtime)

                implementation(libs.firebase.analytics)

                implementation(project.dependencies.platform(libs.supabase.bom))
                implementation(libs.supabase.auth)
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
                implementation(libs.firebase.crashlytics)
                implementation(libs.mixpanel)
            }
        }
    }
}

android {
    namespace = "com.kafka.analytics"
}
