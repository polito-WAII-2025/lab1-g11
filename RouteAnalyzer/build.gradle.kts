plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.1.10"
    application
}

group = "g11"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.3")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("g11.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.jar {
    manifest{
        attributes["Main-Class"] = "g11.MainKt"
    }
}

