package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraGradleWrapperExtension @Inject constructor(objects: ObjectFactory) {

    public val baseUrl: Property<String> = objects.property<String>().convention(GRADLE_WRAPPER_DIST_URL)
    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_WRAPPER)

    public companion object {
        public const val EXTRA_GRADLE_WRAPPER_EXTENSION_NAME: String = "gradleWrapperExt"

        public const val GRADLE_WRAPPER_DIST_URL: String = "https://services.gradle.org/distributions/"
        public const val DEFAULT_ENABLED_EXTRA_WRAPPER: Boolean = true
    }
}