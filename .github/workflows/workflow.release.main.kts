#!/usr/bin/env kotlin

// github-workflows-kt
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.2.0")
@file:Repository("https://bindings.krzeminski.it")

// Actions
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("softprops:action-gh-release:v2")
@file:DependsOn("dawidd6:action-get-tag:v1")

import Environment.GITHUB_TOKEN_ENV
import Environment.GRADLE_PUBLISH_KEY_ENV
import Environment.GRADLE_PUBLISH_SECRET_ENV
import Environment.REPO_PASSWORD_ENV
import Environment.REPO_USERNAME_ENV
import Environment.SIGNING_KEY_ENV
import Environment.SIGNING_KEY_ID_ENV
import Environment.SIGNING_PASSWORD_ENV
import Environment.SIGNING_PUB_KEY_ENV
import Secrets.GRADLE_PUBLISH_KEY
import Secrets.GRADLE_PUBLISH_SECRET
import Secrets.MAVEN_SONATYPE_SIGNING_KEY_ASCII_ARMORED
import Secrets.MAVEN_SONATYPE_SIGNING_KEY_ID
import Secrets.MAVEN_SONATYPE_SIGNING_PASSWORD
import Secrets.MAVEN_SONATYPE_SIGNING_PUB_KEY_ASCII_ARMORED
import Secrets.MAVEN_SONATYPE_TOKEN
import Secrets.MAVEN_SONATYPE_USERNAME
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.Checkout.FetchDepth
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.SetupJava.Distribution.Temurin
import io.github.typesafegithub.workflows.actions.dawidd6.ActionGetTag_Untyped
import io.github.typesafegithub.workflows.actions.softprops.ActionGhRelease
import io.github.typesafegithub.workflows.domain.Mode.Write
import io.github.typesafegithub.workflows.domain.Permission.Contents
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.contexts.SecretsContext
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig.Disabled

check(KotlinVersion.CURRENT.isAtLeast(2, 1, 0)) { "This script requires Kotlin 2.1.0 or later. Current: ${KotlinVersion.CURRENT}" }

object Secrets {
    val SecretsContext.GRADLE_PUBLISH_KEY by SecretsContext.propertyToExprPath
    val SecretsContext.GRADLE_PUBLISH_SECRET by SecretsContext.propertyToExprPath

    val SecretsContext.MAVEN_SONATYPE_USERNAME by SecretsContext.propertyToExprPath
    val SecretsContext.MAVEN_SONATYPE_TOKEN by SecretsContext.propertyToExprPath

    val SecretsContext.MAVEN_SONATYPE_SIGNING_KEY_ID by SecretsContext.propertyToExprPath
    val SecretsContext.MAVEN_SONATYPE_SIGNING_PUB_KEY_ASCII_ARMORED by SecretsContext.propertyToExprPath
    val SecretsContext.MAVEN_SONATYPE_SIGNING_KEY_ASCII_ARMORED by SecretsContext.propertyToExprPath
    val SecretsContext.MAVEN_SONATYPE_SIGNING_PASSWORD by SecretsContext.propertyToExprPath
}

object Environment {
    const val GITHUB_TOKEN_ENV = "GITHUB_TOKEN"

    const val GRADLE_PUBLISH_KEY_ENV = "GRADLE_PUBLISH_KEY"
    const val GRADLE_PUBLISH_SECRET_ENV = "GRADLE_PUBLISH_SECRET"

    const val REPO_USERNAME_ENV = "MAVEN_SONATYPE_USERNAME"
    const val REPO_PASSWORD_ENV = "MAVEN_SONATYPE_TOKEN"

    const val SIGNING_KEY_ID_ENV = "ORG_GRADLE_PROJECT_signingKeyId"
    const val SIGNING_PUB_KEY_ENV = "ORG_GRADLE_PROJECT_signingPublicKey"
    const val SIGNING_KEY_ENV = "ORG_GRADLE_PROJECT_signingKey"
    const val SIGNING_PASSWORD_ENV = "ORG_GRADLE_PROJECT_signingPassword"
}

workflow(
    name = "Release",
    on = listOf(Push(tags = listOf("*"))),
    permissions = mapOf(Contents to Write),
    sourceFile = __FILE__,
    targetFileName = "release-on-tag.yml",
    consistencyCheckJobConfig = Disabled
) {
    job(
        id = "Release", name = "Release",
        runsOn = UbuntuLatest
    ) {
        uses(name = "Check out", action = Checkout(fetchDepth = FetchDepth.Value(0)))
        val tag = getGitTag()
        uses(
            name = "Create Release",
            action = ActionGhRelease(
                tagName = expr { tag },
                name = expr { tag },
                draft = false
            ),
            env = mapOf(GITHUB_TOKEN_ENV to expr { secrets.GITHUB_TOKEN }),
        )
        uses(name = "Set up Java", action = SetupJava(javaVersion = "17", distribution = Temurin))
        run(
            name = "Publish",
            command = "./gradlew :plugins:publishPlugins :plugins:aa_catalog:publish -Pversion='${expr { tag }}'",
            env = mapOf(
                REPO_USERNAME_ENV to expr { secrets.MAVEN_SONATYPE_USERNAME },
                REPO_PASSWORD_ENV to expr { secrets.MAVEN_SONATYPE_TOKEN },
                GRADLE_PUBLISH_KEY_ENV to expr { secrets.GRADLE_PUBLISH_KEY },
                GRADLE_PUBLISH_SECRET_ENV to expr { secrets.GRADLE_PUBLISH_SECRET },
                SIGNING_KEY_ID_ENV to expr { secrets.MAVEN_SONATYPE_SIGNING_KEY_ID },
                SIGNING_PUB_KEY_ENV to expr { secrets.MAVEN_SONATYPE_SIGNING_PUB_KEY_ASCII_ARMORED },
                SIGNING_KEY_ENV to expr { secrets.MAVEN_SONATYPE_SIGNING_KEY_ASCII_ARMORED },
                SIGNING_PASSWORD_ENV to expr { secrets.MAVEN_SONATYPE_SIGNING_PASSWORD }
            )
        )
    }
}

fun JobBuilder<*>.getGitTag(): String = uses(
    name = "Get Tag",
    action = ActionGetTag_Untyped()
).outputs.tag
