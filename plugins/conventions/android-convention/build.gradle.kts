@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("android.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.AndroidConventionPlugin"
            id = "io.github.zenhelix.android.convention"
            displayName = "Gradle plugin for Android project setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Android projects"
            tags = listOf("android", "gradle-plugin", "build-tools", "mobile", "configuration", "kotlin")
        }
    }
}
