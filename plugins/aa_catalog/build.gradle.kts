import org.gradle.api.plugins.catalog.VersionCatalogPlugin.GRADLE_PLATFORM_DEPENDENCIES

plugins {
    `version-catalog`
    id("io.github.zenhelix.maven-central-publish")
}

publishing {
    publications {
        create<MavenPublication>("versionCatalog") {
            from(components[GRADLE_PLATFORM_DEPENDENCIES])
            artifactId = "gradle-magic-wands-catalog"
        }
    }
    repositories {
        mavenLocal()
        mavenCentralPortal {
            credentials {
                username = System.getProperty("MAVEN_SONATYPE_USERNAME") ?: System.getenv("MAVEN_SONATYPE_USERNAME")
                password = System.getProperty("MAVEN_SONATYPE_TOKEN") ?: System.getenv("MAVEN_SONATYPE_TOKEN")
            }
        }
    }
}

val currentVersion = rootProject.version.toString()

catalog {
    versionCatalog {
        version("gradle-magic-wands-plugin", currentVersion)

        rootProject.subprojects.filter { it.name != "aa_catalog" }.forEach { project ->
            project.afterEvaluate {
                this.extensions.findByType<GradlePluginDevelopmentExtension>()?.plugins?.forEach {
                    plugin(it.name.replace(".", "-"), it.name).version(currentVersion)
                }
            }
        }
    }
}