gradlePlugin {
    plugins {
        create("kmm-android.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformAndroidConventionPlugin"
            id = "io.github.zenhelix.kmm-android.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform Android setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects specifically for Android"
            tags = listOf("kotlin", "multiplatform", "kmm", "android", "gradle-plugin", "build-tools", "configuration")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}
