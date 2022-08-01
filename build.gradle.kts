plugins {
    java
    id("fabric-loom") version "0.11.+"
}

group = "dev.isxander"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://api.modrinth.com/maven") {
        name = "Modrinth Mods"
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven(url = "https://maven.shedaniel.me/")
}

fun DependencyHandlerScope.includeModImplementation(dependencyNotation: String, dependencyConfiguration: Action<ExternalModuleDependency> = Action<ExternalModuleDependency> {}) {
    include(modImplementation(dependencyNotation, dependencyConfiguration))
}

dependencies {
    val minecraftVersion: String by project
    val yarnVersion: String by project
    val loaderVersion: String by project
    val fabricVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnVersion:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    includeModImplementation("maven.modrinth:gravitychanger:0.3.0")
    includeModImplementation("me.shedaniel.cloth:cloth-config-fabric:8.0.+") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }
}
