gradlePlugin {
    plugins {
        create("kmm-compose.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformConventionPlugin"
            id = "io.github.zenhelix.kmm-compose.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform Compose setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects with Compose Multiplatform"
            tags = listOf("kotlin", "multiplatform", "kmm", "compose", "gradle-plugin", "build-tools", "configuration")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
    compileOnly(pluginsDevPlatform.compose.gradle.plugin)
}
