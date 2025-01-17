gradlePlugin {
    plugins {
        create("kmm-jvm.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformJvmConventionPlugin"
            id = "io.github.zenhelix.kmm-jvm.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform JVM setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects specifically for JVM"
            tags = listOf("kotlin", "multiplatform", "kmm", "jvm", "build-tools")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}
