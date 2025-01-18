@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("android-sdk.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.AndroidCompilationConventionPlugin"
            id = "io.github.zenhelix.android-sdk.convention"
            displayName = "Gradle plugin for Android SDK setup"
            description = "A Gradle plugin that automates the setup and management of Android SDK"
            tags = listOf("android", "sdk", "gradle-plugin", "build-tools", "configuration", "android-development")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.android.tools.gradle.plugin)
}
