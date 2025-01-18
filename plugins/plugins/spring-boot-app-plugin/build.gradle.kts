gradlePlugin {
    plugins {
        create("spring-boot-app") {
            implementationClass = "io.github.zenhelix.gradle.plugin.SpringBootApplicationPlugin"
            id = "io.github.zenhelix.spring-boot-app"
            displayName = "Gradle plugin for Spring Boot setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Spring Boot projects"
            tags = listOf("spring", "spring-boot", "gradle-plugin", "build-tools", "configuration", "java")
        }
    }
}

dependencies {
    implementation(pluginsDevPlatform.spring.boot.gradle.plugin)

    implementation(rootProject.projects.jacocoConvention)
    implementation(rootProject.projects.jacocoReportAggregationConvention)
    implementation(rootProject.projects.publishMavenConvention)
    implementation(rootProject.projects.javaConvention)
    implementation(rootProject.projects.jvmTestSuiteConvention)
    implementation(rootProject.projects.lombokConvention)

    implementation(rootProject.projects.springConvention)
    implementation(rootProject.projects.springBootConvention)

}
