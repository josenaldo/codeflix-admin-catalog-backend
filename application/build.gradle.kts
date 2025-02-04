plugins {
    id("java")
}

group = "br.com.josenaldo.codeflix.application"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
}
