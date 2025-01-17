package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraKotlinJvmExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_KOTLIN_JVM_PLUGIN)

    public companion object {
        public const val EXTRA_KOTLIN_JVM_EXTENSION_NAME: String = "kotlinJvmExt"

        public const val DEFAULT_ENABLED_EXTRA_KOTLIN_JVM_PLUGIN: Boolean = true
    }
}