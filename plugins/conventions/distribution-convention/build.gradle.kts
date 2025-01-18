@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("distribution.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.DistributionConventionPlugin"
            id = "io.github.zenhelix.distribution.convention"
            displayName = "Gradle plugin for Android SDK setup"
            description = "A Gradle plugin that automates the setup and management of Android SDK"
            tags = listOf("android", "sdk", "gradle-plugin", "build-tools", "configuration", "android-development")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
    compileOnly(pluginsDevPlatform.compose.gradle.plugin)
}
