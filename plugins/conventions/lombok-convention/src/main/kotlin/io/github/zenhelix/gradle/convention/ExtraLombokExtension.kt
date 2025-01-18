package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraLombokExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_LOMBOK_PLUGIN)

    public companion object {
        public const val EXTRA_LOMBOK_EXTENSION_NAME: String = "lombokExt"

        public const val DEFAULT_ENABLED_EXTRA_LOMBOK_PLUGIN: Boolean = true
    }
}