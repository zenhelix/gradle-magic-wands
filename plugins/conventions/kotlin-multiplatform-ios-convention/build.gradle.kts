gradlePlugin {
    plugins {
        create("kmm-ios.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformIosConventionPlugin"
            id = "io.github.zenhelix.kmm-ios.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform iOS setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects specifically for iOS"
            tags = listOf("kotlin", "multiplatform", "kmm", "ios", "build-tools")
        }
    }
}
