package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraJacocoExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_JACOCO_PLUGIN)

    public companion object {
        public const val EXTRA_JACOCO_EXTENSION_NAME: String = "jacocoExt"

        public const val DEFAULT_ENABLED_EXTRA_JACOCO_PLUGIN: Boolean = true
    }
}