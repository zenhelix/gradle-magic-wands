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

            pom {
                description = "A catalog of plugins"
                url = "https://github.com/zenhelix/gradle-magic-wands"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/zenhelix/gradle-magic-wands.git"
                    developerConnection = "scm:git:ssh://github.com/zenhelix/gradle-magic-wands.git"
                    url = "https://github.com/zenhelix/gradle-magic-wands"
                }
                developers {
                    developer {
                        id = "dm.medakin"
                        name = "Dmitrii Medakin"
                        email = "dm.medakin.online@gmail.com"
                    }
                }
            }
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

signing {
    // ORG_GRADLE_PROJECT_signingKeyId
    val signingKeyId: String? by project
    // ascii-armored format
    // ORG_GRADLE_PROJECT_signingKey
    val signingKey: String? by project
    // ORG_GRADLE_PROJECT_signingPassword
    val signingPassword: String? by project

    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}

val currentVersion = rootProject.version.toString()

catalog {
    versionCatalog {
        version("gradle-magic-wands-plugin", currentVersion)

        rootProject.subprojects.filter { it.name != "aa_catalog" }.forEach { project ->
            project.afterEvaluate {
                this.extensions.findByType<GradlePluginDevelopmentExtension>()?.plugins?.forEach {
                    plugin(it.name.replace(".", "-"), it.id).version(currentVersion)
                }
            }
        }
    }
}