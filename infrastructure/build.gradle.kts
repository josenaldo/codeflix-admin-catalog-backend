plugins {
    id("java")
    id("application")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "br.com.josenaldo.codeflix.infrastructure"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("com.h2database:h2")

}

tasks {
    bootJar {
        archiveFileName.set("application.jar")
        destinationDirectory.set(file("${rootProject.layout.buildDirectory.get()}/libs"))
    }
}
