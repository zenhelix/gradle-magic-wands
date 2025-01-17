package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraMavenPublishExtension.Companion.EXTRA_MAVEN_PUBLISH_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType

public class PublishMavenConventionPlugin : Plugin<Project> {
    public companion object {
        public const val MAVEN_PUBLISH_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.maven-publish.convention"
    }

    override fun apply(target: Project) {
        val extraMavenPublishExtension = target.extensions.create<ExtraMavenPublishExtension>(EXTRA_MAVEN_PUBLISH_EXTENSION_NAME)

        target.afterEvaluate {
            val enabled = extraMavenPublishExtension.enabled.get()
            if (enabled.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin("maven-publish") {
                extensions.findByType<PublishingExtension>()?.also {
                    it.publications.forEach {
                        it.withBuildIdentifier()
                    }
                }
            }
        }
    }

}