gradlePlugin {
    plugins {
        create("spring-boot.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.SpringBootConventionPlugin"
            id = "io.github.zenhelix.spring-boot.convention"
            displayName = "Gradle plugin for Spring Boot project setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of SpringBoot-based projects"
            tags = listOf("spring", "spring-boot", "gradle-plugin", "build-tools", "configuration", "java", "kotlin")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.spring.boot.gradle.plugin)
    compileOnly(rootProject.projects.springConvention)
}

configurations.compileOnly.get().isCanBeResolved = true
tasks.withType<PluginUnderTestMetadata> {
    pluginClasspath.from(configurations.compileOnly)
}