plugins {
    id("java")
}

group = "br.com.josenaldo.codeflix.application"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

var mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    implementation(project(":domain"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}
