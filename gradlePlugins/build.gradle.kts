buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradlePlug)
        classpath(libs.kotlin.gradlePlug)
        classpath(libs.google.gmsGoogleServices)
        classpath(libs.google.crashlyticsGradle)
        classpath(libs.hilt.gradlePlug)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
