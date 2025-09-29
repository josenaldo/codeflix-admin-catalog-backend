buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("java")
    id("application")

    alias(libs.plugins.spring.boot.plugin)
    alias(libs.plugins.spring.dependency.management.plugin)
}

group = "br.com.josenaldo.codeflix.infrastructure"
version = "0.0.1-SNAPSHOT"

java { // Accessing a convention
    sourceCompatibility = JavaVersion.VERSION_22
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":application"))

    implementation(libs.spring.boot.starter.web) {
        exclude(
            group = libs.spring.boot.starter.tomcat.get().group,
            module = libs.spring.boot.starter.tomcat.get().module.name
        )
    }
    implementation(libs.spring.boot.starter.undertow)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.h2)
    implementation(libs.liquibase.core)
    implementation(libs.springdoc.openapi.starter)
    implementation(libs.afterburner)

    testImplementation(libs.test.containers.core)
    testImplementation(libs.test.containers.mysql)
    testImplementation(libs.test.containers.junit)

    runtimeOnly(libs.mysql.connector.j)
}

tasks {
    bootJar {
        archiveFileName.set("application.jar")
        destinationDirectory.set(file("${rootProject.layout.buildDirectory.get()}/libs"))
    }
}
