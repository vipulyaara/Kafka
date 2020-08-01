include(":logger")
include(":ui-common")
include(":language")
include(":content")
include(":reader")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
}

include(":app")
include(":data")
include(":data-base")
include(":player")
