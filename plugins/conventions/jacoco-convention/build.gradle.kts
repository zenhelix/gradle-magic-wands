@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("jacoco.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.JacocoConventionPlugin"
            id = "io.github.zenhelix.jacoco.convention"
            displayName = "Gradle plugin enhancing jacoco code coverage"
            description =
                "A Gradle plugin that extends the jacoco plugin with additional configurations and reporting features for enhanced code coverage analysis"
            tags = listOf("jacoco", "code-coverage", "testing", "gradle-plugin", "reporting", "quality", "java", "build-tools")
        }
    }
}
