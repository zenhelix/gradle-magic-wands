gradlePlugin {
    plugins {
        create("kotlin-jvm.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinJvmConventionPlugin"
            id = "io.github.zenhelix.kotlin-jvm.convention"
            displayName = "Gradle plugin for Kotlin JVM project setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin JVM projects"
            tags = listOf("kotlin", "jvm", "gradle-plugin", "build-tools", "compilation", "configuration", "kotlin-jvm")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.jvm.gradle.plugin)
}
