package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraJavaExtension.Companion.EXTRA_JAVA_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.CoreJavadocOptions
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType

public class JavaConventionPlugin : Plugin<Project> {

    public companion object {
        public const val JAVA_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.java.convention"

        private const val JAVA_PLUGIN_NAME = "java"
    }

    override fun apply(target: Project) {
        val extraJavaExtension = target.extensions.create<ExtraJavaExtension>(EXTRA_JAVA_EXTENSION_NAME)

        target.afterEvaluate {
            val enabled = extraJavaExtension.enabled.get()
            if (enabled.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin(JAVA_PLUGIN_NAME) {
                extensions.configure<JavaPluginExtension> {
                    if (extraJavaExtension.withJavadocJar.getOrElse(true)) {
                        withJavadocJar()
                        tasks.withType<Javadoc>().configureEach {
                            options {
                                (this as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
                            }
                        }
                    }
                    if (extraJavaExtension.withSourcesJar.getOrElse(true)) {
                        withSourcesJar()
                    }
                }
            }

        }
    }

}