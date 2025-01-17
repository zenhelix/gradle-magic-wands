package io.github.zenhelix.gradle.convention.utils

import java.net.URL
import java.util.jar.Manifest
import kotlin.reflect.KClass

internal object ManifestUtils {

    fun getManifest(clazz: KClass<*>): Manifest? {
        val clz = clazz.java
        val resource = "/" + clz.name.replace(".", "/") + ".class"
        val fullPath = clz.getResource(resource)!!.toString()
        var archivePath = fullPath.take(fullPath.length - resource.length)
        if (archivePath.endsWith("\\WEB-INF\\classes") || archivePath.endsWith("/WEB-INF/classes")) {
            archivePath = archivePath.take(archivePath.length - "/WEB-INF/classes".length)
        }
        return try {
            URL("$archivePath/META-INF/MANIFEST.MF").openStream().use { input -> Manifest(input) }
        } catch (e: Exception) {
            null
        }
    }
}