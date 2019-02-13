import Kafka.groupId
import com.android.build.gradle.BaseExtension
import com.dicedmelon.gradle.jacoco.android.JacocoAndroidUnitTestReportExtension
import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.support.ReporterType
import org.gradle.api.publish.maven.MavenPom
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    java
    kotlin("jvm") version Kotlin.version apply false
    id(Android.libPlugin) version Android.version apply false
    id(Jacoco.Android.plugin) version Jacoco.Android.version apply false
    id(KotlinX.Serialization.plugin) version Kotlin.version apply false
    id(Ktlint.plugin) version Ktlint.version apply false

    `maven-publish`
    id(Release.Bintray.plugin) version Release.Bintray.version
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        jcenter()
    }
}

val androidModules = listOf("data", "kafka")
val androidSampleModules = listOf("app")

subprojects {
    val isAndroidModule = project.name in androidModules
    val isSample = project.name in androidSampleModules
    val isJvmModule = !isAndroidModule && !isSample

    if (isJvmModule) {
        apply {
            plugin(Kotlin.plugin)
            plugin(Jacoco.plugin)
        }

        configure<JacocoPluginExtension> {
            toolVersion = Jacoco.version
        }

        dependencies {
            implementation(Kotlin.stdlib)
            testImplementation(JUnit.dependency)
        }

        configure<JavaPluginConvention> {
            sourceCompatibility = JavaVersion.VERSION_1_7
            targetCompatibility = JavaVersion.VERSION_1_7

            sourceSets {
                getByName("main").java.srcDirs("src/main/kotlin")
                getByName("test").java.srcDirs("src/main/kotlin")
            }
        }

        tasks.withType<JacocoReport> {
            reports {
                html.isEnabled = false
                xml.isEnabled = true
                csv.isEnabled = false
            }
        }

        val sourcesJar by tasks.registering(Jar::class) {
            from(sourceSets["main"].allSource)
            classifier = "sources"
        }

        val doc by tasks.creating(Javadoc::class) {
            isFailOnError = false
            source = sourceSets["main"].allJava
        }
    }

    if (isAndroidModule) {
        apply {
            plugin(Android.libPlugin)
            plugin(Kotlin.androidPlugin)
            plugin(Kotlin.androidExtensionsPlugin)
            plugin(Jacoco.Android.plugin)
        }

        configure<BaseExtension> {
            compileSdkVersion(Kafka.compileSdkVersion)

            defaultConfig {
                minSdkVersion(Kafka.minSdkVersion)
                targetSdkVersion(Kafka.compileSdkVersion)
                versionCode = 1
                versionName = Kafka.publishVersion
            }

            sourceSets {
                getByName("main").java.srcDirs("src/main/kotlin")
                getByName("test").java.srcDirs("src/test/kotlin")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_7
                setTargetCompatibility(JavaVersion.VERSION_1_7)
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

            testOptions {
                unitTests.isReturnDefaultValues = true
            }

            val sourcesJar by tasks.registering(Jar::class) {
                from(sourceSets["main"].java.srcDirs)
                classifier = "sources"
            }

            val doc by tasks.creating(Javadoc::class) {
                isFailOnError = false
                source = sourceSets["main"].java.sourceFiles
                classpath += files(bootClasspath.joinToString(File.pathSeparator))
                classpath += configurations.compile
            }
        }

        configure<JacocoAndroidUnitTestReportExtension> {
            csv.enabled(false)
            html.enabled(true)
            xml.enabled(true)
        }
    }

    if (!isSample) {
        apply {
            plugin(Release.MavenPublish.plugin)
            plugin(Release.Bintray.plugin)
            plugin(Ktlint.plugin)
        }

        configure<KotlinterExtension> {
            reporters = arrayOf(ReporterType.plain.name, ReporterType.checkstyle.name)
        }

//        tasks.named<LintTask>("lintKotlinMain") {
//            enabled = false
//        }
//
//        tasks.named<LintTask>("lintKotlinTest") {
//            enabled = false
//        }

        version = Kafka.publishVersion
        group = Kafka.groupId
        bintray {
            user = findProperty("BINTRAY_USER") as? String
            key = findProperty("BINTRAY_KEY") as? String
            setPublications(project.name)
            with(pkg) {
                repo = "maven"
                name = "Kafka-Android"
                desc = "The dream book reading application"
                userOrg = "airtel"
                websiteUrl = ""
                vcsUrl = ""
                setLicenses("MIT")
                with(version) {
                    name = Kafka.publishVersion
                }
            }
        }

        fun MavenPom.addDependencies() = withXml {
            asNode().appendNode("dependencies").let { depNode ->
                configurations.implementation.allDependencies.forEach {
                    depNode.appendNode("rxJava2").apply {
                        appendNode("groupId", it.group)
                        appendNode("artifactId", it.name)
                        appendNode("version", it.version)
                    }
                }
            }
        }

        val javadocJar by tasks.creating(Jar::class) {
            val doc by tasks
            dependsOn(doc)
            from(doc)

            classifier = "javadoc"
        }

        val sourcesJar by tasks
        publishing {
            publications {
                register(project.name, MavenPublication::class) {
                    if (project.hasProperty("android")) {
                        artifact("$buildDir/outputs/aar/${project.name}-release.aar")
                    } else {
                        from(components["java"])
                    }
                    artifact(sourcesJar)
                    artifact(javadocJar)
                    groupId = Kafka.groupId
                    artifactId = project.name
                    version = Kafka.publishVersion

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
}
