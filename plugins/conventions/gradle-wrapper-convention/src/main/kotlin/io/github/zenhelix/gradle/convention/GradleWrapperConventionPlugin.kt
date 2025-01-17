package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraGradleWrapperExtension.Companion.DEFAULT_ENABLED_EXTRA_WRAPPER
import io.github.zenhelix.gradle.convention.ExtraGradleWrapperExtension.Companion.EXTENSION_NAME
import io.github.zenhelix.gradle.convention.ExtraGradleWrapperExtension.Companion.GRADLE_WRAPPER_DIST_URL
import io.github.zenhelix.gradle.convention.utils.ManifestUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.util.GradleVersion
import org.gradle.util.internal.DistributionLocator
import java.net.URI

public class GradleWrapperConventionPlugin : Plugin<Project> {

    public companion object {
        public const val GRADLE_WRAPPER_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.gradle-wrapper.convention"
        public const val DEFAULT_FALLBACK_VERSION: String = "8.11.1"

        /**
         * @see org.gradle.util.internal.DistributionLocator.SERVICES_GRADLE_BASE_URL_PROPERTY
         */
        public const val GRADLE_BASE_URL_PROPERTY_NAME: String = "org.gradle.internal.services.base.url"
        private const val FAKE_VALUE_VERSION = "42.666"
    }

    override fun apply(target: Project) {
        val rootProject = target.rootProject

        if (rootProject.extensions.findByType<ExtraGradleWrapperExtension>() != null) {
            return
        }

        val extraGradleWrapperExtension = rootProject.extensions.create<ExtraGradleWrapperExtension>(EXTENSION_NAME)

        var defaultVersion: String = GradleVersion.current().version
        rootProject.tasks.named<Wrapper>("wrapper") {
            defaultVersion = gradleVersion
            gradleVersion = FAKE_VALUE_VERSION
        }

        rootProject.afterEvaluate {
            val baseUrl = extraGradleWrapperExtension.baseRepositoryUrl.getOrElse(GRADLE_WRAPPER_DIST_URL).let { URI.create(it) }
            val enabled = extraGradleWrapperExtension.enabled.getOrElse(DEFAULT_ENABLED_EXTRA_WRAPPER)
            if (enabled.not()) {
                tasks.withType<Wrapper>().configureEach {
                    gradleVersion = defaultVersion
                }
                return@afterEvaluate
            }

            val gradleBaseUrl = if (System.getProperty(GRADLE_BASE_URL_PROPERTY_NAME) == null) {
                DistributionLocator.getBaseUrl()
            } else {
                null
            }
            tasks.withType<Wrapper>().configureEach {
                val currentVersion = gradleVersion
                if (currentVersion == FAKE_VALUE_VERSION) {
                    gradleVersion = gradleVersion()
                }

                if (gradleBaseUrl != null && distributionUrl.contains(gradleBaseUrl)) {
                    distributionUrl =
                        baseUrl.resolve("gradle-${gradleVersion}-${distributionType.name.lowercase()}.zip").toURL().toString()
                }
            }
        }
    }

    private fun gradleVersion(): String =
        ManifestUtils.getManifest(this::class)?.mainAttributes?.getValue("Gradle-Version") ?: DEFAULT_FALLBACK_VERSION

}