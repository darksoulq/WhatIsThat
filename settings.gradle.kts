pluginManagement {
    repositories {
        maven("https://maven.kikugie.dev/snapshots")
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
}

rootProject.name = "WIT"

stonecutter {
    create(rootProject) {
        versions("26.3", "26.2", "26.1.2", "1.21.11")
        vcsVersion.set("26.2")
    }
}

include("1.21.11")
include("26.1.2")
include("26.2")
include("26.3")