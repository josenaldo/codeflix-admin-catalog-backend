plugins {
	java
    alias(libs.plugins.spring.boot.plugin)
    alias(libs.plugins.spring.dependency.management.plugin)
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
    developmentOnly(libs.spring.boot.devtools)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

subprojects {
    val mockitoAgent = configurations.create("mockitoAgent")

    apply(plugin = "java")
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.plugin.get().pluginId)

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
//            mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
        }
    }

    dependencies {

        implementation(rootProject.libs.ulid)
        implementation(rootProject.libs.vavr)

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testImplementation(rootProject.libs.spring.boot.starter.test)
        testRuntimeOnly(rootProject.libs.junit.platform.launcher)
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
