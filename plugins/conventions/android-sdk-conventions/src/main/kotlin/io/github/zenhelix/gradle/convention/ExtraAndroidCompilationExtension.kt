package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraAndroidCompilationExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_ANDROID_COMPILATION_PLUGIN)

    public val minSdk: Property<Int> = objects.property<Int>().convention(DEFAULT_MIN_SDK)
    public val compileSdk: Property<Int> = objects.property<Int>().convention(DEFAULT_COMPILE_SDK)
    public val targetSdk: Property<Int> = objects.property<Int>().convention(DEFAULT_TARGET_SDK)

    public companion object {
        public const val EXTRA_ANDROID_EXTENSION_NAME: String = "androidExt"

        public const val DEFAULT_ENABLED_EXTRA_ANDROID_COMPILATION_PLUGIN: Boolean = true

        public const val DEFAULT_MIN_SDK: Int = 21
        public const val DEFAULT_COMPILE_SDK: Int = 34
        public const val DEFAULT_TARGET_SDK: Int = DEFAULT_COMPILE_SDK
    }

}