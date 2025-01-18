package io.github.zenhelix.gradle.plugin

import io.github.zenhelix.gradle.convention.JacocoConventionPlugin
import io.github.zenhelix.gradle.convention.JacocoReportAggregationConventionPlugin
import io.github.zenhelix.gradle.convention.JavaConventionPlugin
import io.github.zenhelix.gradle.convention.JvmTestSuiteConventionPlugin
import io.github.zenhelix.gradle.convention.LombokConventionPlugin
import io.github.zenhelix.gradle.convention.PublishMavenConventionPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin

@Suppress("UnstableApiUsage")
public class JavaLibraryPlugin : Plugin<Project> {

    public companion object {
        public const val JAVA_LIBRARY_PLUGIN_ID: String = "io.github.zenhelix.java-library"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(JavaLibraryPlugin::class)

            apply(JavaPlugin::class)
            apply(JavaConventionPlugin::class)

            apply(LombokConventionPlugin::class)

            apply(JacocoReportAggregationPlugin::class)
            apply(JacocoReportAggregationConventionPlugin::class)
            apply(JacocoPlugin::class)
            apply(JacocoConventionPlugin::class)

            apply(MavenPublishPlugin::class)
            apply(PublishMavenConventionPlugin::class)

            apply("jvm-test-suite") // Also needed to support JacocoReportAggregationPlugin
            apply(JvmTestSuiteConventionPlugin::class)
        }
    }

}