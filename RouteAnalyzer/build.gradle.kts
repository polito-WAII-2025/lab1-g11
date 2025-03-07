plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "g11"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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

