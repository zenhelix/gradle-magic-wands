@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("jacoco-report-aggregation.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.JacocoReportAggregationConventionPlugin"
            id = "io.github.zenhelix.jacoco-report-aggregation.convention"
            displayName = "Gradle plugin enhancing jacoco-report-aggregation"
            description =
                "A Gradle plugin that simplifies and extends the jacoco-report-aggregation plugin, enabling consolidated code coverage reports across multiple projects or modules."
            tags = listOf("jacoco", "jacoco-report-aggregation", "code-coverage", "testing", "gradle-plugin", "reporting", "multi-module", "quality")
        }
    }
}
