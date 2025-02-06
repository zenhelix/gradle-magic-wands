gradlePlugin {
    plugins {
        create("spring-boot-autoconfigure-library") {
            implementationClass = "io.github.zenhelix.gradle.plugin.SpringBootAutoconfigureLibraryPlugin"
            id = "io.github.zenhelix.spring-boot-autoconfigure-library"
            displayName = "Gradle plugin for Spring Boot Autoconfigure setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Spring Boot Autoconfigure projects"
            tags = listOf("spring", "spring-boot", "starter", "gradle-plugin", "build-tools", "configuration", "kotlin")
        }
    }
}

dependencies {
    implementation(rootProject.projects.springLibraryPlugin)
    implementation(rootProject.projects.springConvention)
}
