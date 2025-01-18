gradlePlugin {
    plugins {
        create("kmm-js.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformJsConventionPlugin"
            id = "io.github.zenhelix.kmm-js.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform JS setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects specifically for JavaScript"
            tags = listOf("kotlin", "multiplatform", "kmm", "js", "build-tools")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}
