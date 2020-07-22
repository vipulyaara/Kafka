import com.android.build.gradle.BaseExtension


plugins {
    java
    kotlin("jvm") version Kotlin.version apply false
    `maven-publish`
}

buildscript {
    extra["kotlin_version"] = "1.3.72"
    extra["compose_version"] = "0.1.0-dev14"
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath(Android.gradlePlugin)
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${AndroidX.Navigation.pluginVersion}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }

    val commonCompilerArgs = listOfNotNull(
        "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    )

    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
        kotlinOptions {
            allWarningsAsErrors = false
            freeCompilerArgs = commonCompilerArgs
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }
}

val libraryModules = listOf(
    Libs.Data.name,
    Libs.BaseData.name,
    Libs.Player.name,
    Libs.UiCommon.name,
    Libs.UiCompose.name,
    Libs.Reader.name,
    Libs.Content.name,
    Libs.Language.name,
    Libs.Logger.name
)
val publishableModules = listOf(Libs.Player.name)
val applicationModules = listOf("app")

subprojects {
    val isLibrary = project.name in libraryModules
    val isPublishable = project.name in publishableModules
    val isAndroid = project.name in libraryModules || project.name in applicationModules
    val isApplication = project.name in applicationModules

    if (isAndroid) {

        if (isLibrary) {
            apply {
                plugin(Android.libPlugin)
                plugin(Kotlin.androidPlugin)
                plugin(Kotlin.androidExtensionsPlugin)
                plugin(Kotlin.kapt)
                plugin(Release.MavenPublish.plugin)
                plugin(Hilt.plugin)
                plugin("androidx.navigation.safeargs.kotlin")
            }
        }

        if (isPublishable) {
            apply {
                plugin(Release.MavenPublish.plugin)
            }
            fun org.gradle.api.publish.maven.MavenPom.addDependencies() = withXml {
                asNode().appendNode("dependencies").let { depNode ->
                    configurations.implementation.allDependencies.forEach {
                        depNode.appendNode("dependency").apply {
                            appendNode("groupId", it.group)
                            appendNode("artifactId", it.name)
                            appendNode("version", it.version)
                        }
                    }
                }
            }

            publishing {
                publications {
                    register(project.name, MavenPublication::class) {
                        if (project.hasProperty("android")) {
                            artifact("$buildDir/outputs/aar/${project.name}-release.aar") {
                                builtBy(tasks.getByPath("assemble"))
                            }
                        } else {
                            from(components["java"])
                        }
//                        artifact(sourcesJar)
                        groupId = Libs.groupId
                        artifactId = project.name
                        version = Libs.publishVersion

                        pom {
                            licenses {
                                license {
                                    name.set("MIT License")
                                    url.set("http://www.opensource.org/licenses/mit-license.php")
                                }
                            }
                        }

                        if (project.hasProperty("android")) {
                            pom.addDependencies()
                        }
                    }
                }
            }
        }

        if (isApplication) {
            apply {
                plugin(Android.appPlugin)
                plugin(Kotlin.androidPlugin)
                plugin(Hilt.plugin)
                plugin(Kotlin.kapt)
                plugin(Kotlin.androidExtensionsPlugin)
                plugin("androidx.navigation.safeargs.kotlin")
            }
        }

        configure<BaseExtension> {
            compileSdkVersion(Libs.compileSdkVersion)

            defaultConfig {
                minSdkVersion(Libs.minSdkVersion)
                targetSdkVersion(Libs.compileSdkVersion)
                vectorDrawables.useSupportLibrary = true
                multiDexEnabled = true
                versionCode = Libs.versionCode
                versionName = Libs.versionName
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            sourceSets {
                getByName("main").java.srcDirs("src/main/kotlin")
                getByName("test").java.srcDirs("src/test/kotlin")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    consumerProguardFiles("proguard-rules.pro")
                }
            }

            lintOptions {
                isAbortOnError = false
            }

            packagingOptions {
                exclude("META-INF/LICENSE.txt")
                exclude("META-INF/NOTICE.txt")
            }
        }
    }
}
