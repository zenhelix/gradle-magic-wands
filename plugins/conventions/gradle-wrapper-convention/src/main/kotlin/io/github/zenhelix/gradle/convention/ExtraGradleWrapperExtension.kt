package io.github.zenhelix.gradle.convention

import org.gradle.api.provider.Property

public abstract class ExtraGradleWrapperExtension {

    public abstract val baseRepositoryUrl: Property<String>
    public abstract val enabled: Property<Boolean>

    public companion object {
        public const val EXTENSION_NAME: String = "extraGradleWrapper"

        public const val GRADLE_WRAPPER_DIST_URL: String = "https://services.gradle.org/distributions/"
        public const val DEFAULT_ENABLED_EXTRA_WRAPPER: Boolean = true
    }
}