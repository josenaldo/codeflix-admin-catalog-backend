buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.liquibase.core)
        classpath(libs.mysql.connector.j)
    }
}

plugins {
    id("java")
    id("application")

    alias(libs.plugins.spring.boot.plugin)
    alias(libs.plugins.spring.dependency.management.plugin)
    alias(libs.plugins.liquibase.plugin)
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

    val liquibaseRuntime: Configuration by configurations

    liquibaseRuntime(libs.mysql.connector.j)
    liquibaseRuntime(libs.picocli)
    liquibaseRuntime(libs.spring.boot.starter.data.jpa)
    liquibaseRuntime(libs.liquibase.core)
}

val changelogFile = "src/main/resources/config/liquibase/master.yaml"
val databasePort: Int = 33064
val databaseName = "codeflix_adm_videos"
val databaseUser = "root"
val databasePassword = "root"
val databaseUrl = "jdbc:mysql://localhost:33064/codeflix_adm_videos"
val databaseDriver = "com.mysql.cj.jdbc.Driver"

liquibase {

//    activities.register("main") {
//        this.arguments = mapOf(
//            "changeLogFile" to changelogFile,
//            "url" to databaseUrl,
//            "username" to databaseUser,
//            "password" to databasePassword,
//            "classpath" to rootDir.toString(),
//            "driver" to databaseDriver,
//            "logLevel" to "info",
//            "referenceUrl" to (
//                "hibernate:spring:com.org.choosemysnooze" +
//                    "?dialect=org.hibernate.dialect.M MySQLDialect" +
//                    "&hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy" +
//                    "&hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
//                )
//
//        )
//    }

    activities.register("main") {
        arguments = mapOf(
            "changelogFile" to changelogFile,
            "url" to databaseUrl,
            "username" to databaseUser,
            "password" to databasePassword,
            "driver" to databaseDriver,
            "logLevel" to "info"
        )
    }
}

tasks {
    bootJar {
        archiveFileName.set("application.jar")
        destinationDirectory.set(file("${rootProject.layout.buildDirectory.get()}/libs"))
    }
}
