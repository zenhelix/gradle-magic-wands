gradlePlugin {
    plugins {
        create("java-library") {
            implementationClass = "io.github.zenhelix.gradle.plugin.JavaLibraryPlugin"
            id = "io.github.zenhelix.java-library"
            displayName = "Gradle plugin for Java library setup."
            description = "A Gradle plugin that simplifies the setup and configuration of Java libraries, including compilation, testing, and publishing, with built-in conventions and utilities."
            tags =  listOf("java", "library","java-library", "gradle-plugin", "build-tools", "compilation", "testing", "publishing", "configuration", "jvm")
        }
    }
}

dependencies {
    implementation(rootProject.projects.jacocoConvention)
    implementation(rootProject.projects.jacocoReportAggregationConvention)
    implementation(rootProject.projects.javaConvention)
    implementation(rootProject.projects.jvmTestSuiteConvention)
    implementation(rootProject.projects.publishMavenConvention)
    implementation(rootProject.projects.lombokConvention)
}
