package com.roadrunner.gradle.plugins

import com.android.build.gradle.LibraryExtension
import com.roadrunner.gradle.Configurations
import com.roadrunner.gradle.extentions.getDep
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("UnstableApiUsage")
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.android.library")
        project.pluginManager.apply("kotlin-android")
        project.pluginManager.apply("org.gradle.android.cache-fix")
        project.pluginManager.apply(JacocoReportPlugin::class.java)

        project.extensions.configure<LibraryExtension>("android") {
            it.compileSdk = Configurations.Android.compileSdkVersion

            it.defaultConfig {
                minSdk = Configurations.Android.minSdkVersion

                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            project.tasks.withType(KotlinCompile::class.java) { kotlinCompile ->
                kotlinCompile.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
            }

            it.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            it.testOptions {
                unitTests.isReturnDefaultValues = true
                unitTests.isIncludeAndroidResources = true
            }


            it.packagingOptions {
                resources.excludes.add("**/attach_hotspot_windows.dll")
                resources.excludes.add("META-INF/DEPENDENCIES")
                resources.excludes.add("META-INF/LICENSE")
                resources.excludes.add("META-INF/LICENSE.txt")
                resources.excludes.add("META-INF/license.txt")
                resources.excludes.add("META-INF/NOTICE")
                resources.excludes.add("META-INF/NOTICE.txt")
                resources.excludes.add("META-INF/notice.txt")
                resources.excludes.add("META-INF/ASL2.0")
                resources.excludes.add("META-INF/AL2.0")
                resources.excludes.add("META-INF/LGPL2.1")
                resources.excludes.add("META-INF/licenses/ASM")
            }

            it.sourceSets {
                named("test") { sourceSet ->
                    sourceSet.java.setSrcDirs(listOf("src/test/java", "src/sharedTest/java"))
                }

                named("androidTest") { sourceSet ->
                    sourceSet.java.setSrcDirs(
                        listOf(
                            "src/androidTest/java",
                            "src/screenshotTest/java",
                            "src/sharedTest/java"
                        )
                    )
                }
            }
        }


        if (!project.file("${project.projectDir}/src/androidTest").exists()) {
            project
                .tasks
                .matching {
                    it.name.startsWith("kapt") && it.name.endsWith("TestKotlin")
                }
                .configureEach { it.enabled = false }
        }

        project
            .tasks
            .withType(Test::class.java) { test ->
                test.useJUnitPlatform()
            }

        val libs = (project.extensions.getByType(VersionCatalogsExtension::class.java)).named("libs")
        project.dependencies.add("implementation", libs.getDep("core_ktx"))
        project.dependencies.add("testImplementation", libs.getDep("junit_jupiter_api"))
        project.dependencies.add("testImplementation", libs.getDep("junit_jupiter_params"))
        project.dependencies.add("testImplementation", libs.getDep("mockito_inline"))
        project.dependencies.add("testImplementation", libs.getDep("mockito_kotlin"))
        project.dependencies.add("testRuntimeOnly", libs.getDep("junit_jupiter_engine"))
        project.dependencies.add("testRuntimeOnly", libs.getDep("junit_vintage_engine"))
    }
}
