plugins {
    id("java")
    id("application")
    id("org.springframework.boot")
}

group = "br.com.josenaldo.codeflix.infrastructure"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.test {
    useJUnitPlatform()
}
