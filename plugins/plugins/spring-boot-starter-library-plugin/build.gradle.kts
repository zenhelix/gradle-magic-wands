gradlePlugin {
    plugins {
        create("spring-boot-starter") {
            implementationClass = "io.github.zenhelix.gradle.plugin.SpringBootStarterLibraryPlugin"
            id = "io.github.zenhelix.spring-boot-starter"
            displayName = "Gradle plugin for Spring Boot Starter setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Spring Boot Starter projects"
            tags = listOf("spring", "spring-boot", "starter", "gradle-plugin", "build-tools", "configuration", "kotlin")
        }
    }
}

dependencies {
    implementation(rootProject.projects.springBootAutoconfigureLibraryPlugin)
}