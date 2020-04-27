include(":ui-common")
include(":language")
include(":search")
include(":domain")
include(":reader")
include(":ui-compose")
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
