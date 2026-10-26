import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.LinkedHashMap
import net.thebugmc.gradle.sonatypepublisher.PublishingType

buildscript {
    repositories { mavenCentral() }
    dependencies { classpath("org.yaml:snakeyaml:2.2") }
}

plugins {
    `java-library`
    signing
    `maven-publish`
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "io.github.darksoulq"
val mcVersion = stonecutter.current.project
version = "1.9.3-mc.${mcVersion}"

val yamlParser = Yaml()

fun parseYaml(f: File): Map<String, Any> {
    if (!f.exists()) return emptyMap()
    val obj = yamlParser.load<Any>(f.readText())
    @Suppress("UNCHECKED_CAST")
    return (obj as? Map<String, Any>) ?: emptyMap()
}

fun readLinesSafe(f: File): List<String> {
    if (!f.exists()) return emptyList()
    return f.readLines().map { it.trim() }.filter { it.isNotEmpty() }
}

fun mergeMaps(global: Map<String, Any>, specific: Map<String, Any>, removals: List<String>): Map<String, Any> {
    val result = LinkedHashMap(global)
    specific.forEach { (k, v) ->
        if (v.toString().trim() == "remove") result.remove(k)
        else result[k] = v
    }
    removals.forEach { result.remove(it) }
    return result
}

val versionsMap = parseYaml(File(rootDir, "gradle/versions.yml"))
val activeConfig = (versionsMap[mcVersion] as? Map<*, *>)
?: (versionsMap["26.2"] as? Map<*, *>)
?: mapOf("java" to 25, "paperweight" to "26.2.build.+", "apiVersion" to "26.2")

val targetJavaVersion = activeConfig["java"]?.toString()?.toIntOrNull() ?: 25
val paperweightStr = activeConfig["paperweight"]?.toString()?.replace("{version}", mcVersion) ?: ""
val apiVersionStr = activeConfig["apiVersion"]?.toString()?.replace("{version}", mcVersion) ?: ""

val activeExcludes = (
        readLinesSafe(File(rootDir, "gradle/excludes/global.txt")) +
                readLinesSafe(File(rootDir, "gradle/excludes/${mcVersion}/excludes.txt"))
).minus(readLinesSafe(File(rootDir, "gradle/excludes/${mcVersion}/removal.txt")).toSet())

sourceSets {
    main {
        activeExcludes.forEach { java.exclude(it) }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    withSourcesJar()
    withJavadocJar()
}

val activeRepos = mergeMaps(
        parseYaml(File(rootDir, "gradle/repos/global.yml")),
        parseYaml(File(rootDir, "gradle/repos/${mcVersion}/repos.yml")),
        readLinesSafe(File(rootDir, "gradle/repos/${mcVersion}/removal.txt"))
)

repositories {
    activeRepos.forEach { (name, url) ->
        if (url == "default") {
            if (name == "mavenCentral") mavenCentral()
        } else {
            maven(url.toString()) { this.name = name }
        }
    }
}

val activeLibs = mergeMaps(
        parseYaml(File(rootDir, "gradle/libs/global.yml")),
        parseYaml(File(rootDir, "gradle/libs/${mcVersion}/libs.yml")),
        readLinesSafe(File(rootDir, "gradle/libs/${mcVersion}/removal.txt"))
)

dependencies {
    paperweight.paperDevBundle(paperweightStr)

    activeLibs.forEach { (_, notation) ->
            when (notation) {
                is Map<*, *> -> {
                    val type = (notation["type"] as? String) ?: "compileOnly"
                    val group = notation["group"]
                    val name = notation["name"] ?: notation["module"]
                    val ver = notation["version"]
                    val dep = if (group != null && name != null && ver != null) "$group:$name:$ver" else notation["module"].toString()
                    add(type, dep)
                }
                is String -> add("compileOnly", notation)
            }
    }
}

runPaper { folia.registerTask() }

tasks {
    withType<Jar>().configureEach {
        archiveVersion.set(project.version.toString())
        archiveBaseName.set("WIT")
        activeExcludes.forEach { exclude(it) }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    named("assemble") {
        dependsOn("sourcesJar", "javadocJar")
    }

    named<xyz.jpenilla.runpaper.task.RunServer>("runServer") {
        minecraftVersion(mcVersion)
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    named<xyz.jpenilla.runpaper.task.RunServer>("runFolia") {
        minecraftVersion(mcVersion)
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    processResources {
        val props = mapOf("version" to project.version.toString(), "apiVersion" to apiVersionStr)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") { expand(props) }
    }

    val javadocExcludesList = readLinesSafe(File(rootDir, "gradle/javadoc-excludes.txt"))

    javadoc {
        val standardOptions = options as StandardJavadocDocletOptions
        standardOptions.apply {
            encoding = "UTF-8"
            charSet = "UTF-8"
            memberLevel = JavadocMemberLevel.PUBLIC
            isAuthor = true
            isVersion = true
            links("https://docs.oracle.com/en/java/javase/$targetJavaVersion/docs/api/")
        }
        exclude(javadocExcludesList)
    }
}

centralPortal {
    name = "WIT"
    publishingType = PublishingType.AUTOMATIC
    pom {
        name = "WIT"
        description = "WIT Minecraft Plugin"
        url = "https://github.com/darksoulq/WIT"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "darksoulq"
                name = "darksoulq"
            }
        }
        scm {
            connection = "scm:git:git://github.com/darksoulq/WIT.git"
            developerConnection = "scm:git:ssh://github.com/darksoulq/WIT.git"
            url = "https://github.com/darksoulq/WIT"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("snapshot") {
            from(components["java"])
            artifactId = "WIT"

            pom {
                name.set("WIT")
                description.set("WIT Minecraft Plugin")
                url.set("https://github.com/darksoulq/WIT")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("darksoulq")
                        name.set("darksoulq")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/darksoulq/WIT.git")
                    developerConnection.set("scm:git:ssh://github.com/darksoulq/WIT.git")
                    url.set("https://github.com/darksoulq/WIT")
                }
            }
        }
    }
    repositories {
        maven {
            name = "SonatypeSnapshots"
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            credentials {
                username = project.findProperty("centralPortal.username") as String?
                        password = project.findProperty("centralPortal.password") as String?
            }
        }
    }
}

signing {
    val keyFile = rootProject.file("sonatype.asc")
    if (keyFile.exists()) {
        useInMemoryPgpKeys(
                project.providers.gradleProperty("signing.keyId").orNull,
                keyFile.readText(),
                project.providers.gradleProperty("signing.password").orNull
        )
    }
}