package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraProguardExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_PROGUARD_PLUGIN)
    public val outputBaseDir: Property<String> = objects.property<String>().convention(DEFAULT_OUTPUT_BASE_DIR)

    public companion object {
        public const val EXTRA_PROGUARD_EXTENSION_NAME: String = "proguardExt"

        public const val DEFAULT_ENABLED_EXTRA_PROGUARD_PLUGIN: Boolean = true
        public const val DEFAULT_PROGUARD_VERSION: String =  "7.6.1"

        public const val DEFAULT_OUTPUT_BASE_DIR: String = "proguard"
    }
}