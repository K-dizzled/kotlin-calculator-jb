buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.18.3")
    }
}

plugins {
    kotlin("jvm") version "1.7.10"
    java
    application
}

tasks {
    test {
        maxHeapSize = "4g"
    }
}

apply(plugin = "kotlinx-atomicfu")

group = "ru.kdizzl.mpp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:lincheck:2.14.1")
    testImplementation("com.amazonaws:aws-java-sdk:1.12.314")
}

sourceSets.main {
    java.srcDir("src")
}

sourceSets.test {
    java.srcDir("test")
}

application {
    mainClass.set("mpp.calculator.CLICalc")
}