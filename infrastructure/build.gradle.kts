buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.flyway.mysql)
    }
}

plugins {
    id("java")
    id("application")

    alias(libs.plugins.spring.boot.plugin)
    alias(libs.plugins.spring.dependency.management.plugin)
    alias(libs.plugins.flyway.plugin)
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
    implementation(libs.h2)
    implementation(libs.flyway.mysql)
    implementation(libs.flyway.gradle.plugin)
    implementation(libs.springdoc.openapi.starter)

    testImplementation(libs.flyway.core)

    runtimeOnly(libs.mysql.connector.j)
}

flyway {
    url = System.getenv("FLYWAY_DB_URL")
        ?: "jdbc:mysql://codeflix-admin-catalog-backend-db:3306/codeflix_adm_videos"
    user = System.getenv("FLYWAY_DB_USER") ?: "root"
    password = System.getenv("FLYWAY_DB_PASSWORD") ?: "root"
    cleanDisabled = false
}

tasks {
    bootJar {
        archiveFileName.set("application.jar")
        destinationDirectory.set(file("${rootProject.layout.buildDirectory.get()}/libs"))
    }
}
