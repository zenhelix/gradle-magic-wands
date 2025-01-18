@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("jvm-test-suite.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.JvmTestSuiteConventionPlugin"
            id = "io.github.zenhelix.jvm-test-suite.convention"
            displayName = "Gradle plugin enhancing jvm-test-suite functionality"
            description =
                "A Gradle plugin that extends the jvm-test-suite plugin with additional configurations and utilities for enhanced JVM testing workflows"
            tags = listOf("jvm-test-suite", "testing", "jvm", "gradle-plugin", "test-automation", "configuration", "build-tools", "java")
        }
    }
}
