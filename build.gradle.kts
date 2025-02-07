plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.josenaldo.codeflix"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

subprojects {
    val mockitoAgent = configurations.create("mockitoAgent")

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
        }
    }

    dependencies {

        implementation("com.github.f4b6a3:ulid-creator:5.2.3")
        implementation("io.vavr:vavr:0.10.6")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
    }

    tasks.test {
        useJUnitPlatform()
        jvmArgs("-javaagent:${mockitoAgent.asPath}")
    }
}

tasks.register<Test>("allTests") {
    description = "Executa todos os testes de todos os módulos"
    group = "verification"

    subprojects.forEach { subproject ->
        dependsOn(subproject.tasks.withType<Test>())
    }
}

tasks {
    bootRun {
        mainClass.set("br.com.josenaldo.codeflix.infrastructure.Application")
        classpath =
            sourceSets["main"].runtimeClasspath + project(":infrastructure").sourceSets["main"].runtimeClasspath
    }
    bootJar {
        mainClass.set("br.com.josenaldo.codeflix.infrastructure.Application")
        dependsOn(":infrastructure:bootJar")
    }
}
