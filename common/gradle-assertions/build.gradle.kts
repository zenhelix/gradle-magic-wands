dependencies {
    api(project.dependencies.gradleTestKit())
    api(platform(pluginsDevPlatform.assertj.bom))
    api("org.assertj:assertj-core")
}