gradlePlugin {
    plugins {
        create("spring-library") {
            implementationClass = "io.github.zenhelix.gradle.plugin.SpringLibraryPlugin"
            id = "io.github.zenhelix.spring-library"
            displayName = "Gradle plugin for Spring library setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Spring library projects"
            tags = listOf("spring", "spring-boot", "gradle-plugin", "build-tools", "configuration", "java")
        }
    }
}

dependencies {
    implementation(rootProject.projects.javaLibraryPlugin)
    implementation(rootProject.projects.springConvention)
}
