import java.util.Properties

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.jxmen"
version = "0.2.0"

tasks.register("writeVersion") {
    // NOTE: git에 태그 추가, API로 버전 확인 시에 사용
    group = "custom"
    description = "Generate a version properties file"

    doLast {
        val resourcesDir = project.layout.projectDirectory.dir("src/main/resources")
        val versionPropertiesFile = resourcesDir.file("version.properties").asFile

        // resources 디렉토리가 없다면 생성
        versionPropertiesFile.parentFile.mkdirs()

        val properties = Properties()
        properties.setProperty("version", project.version.toString())

        versionPropertiesFile.writer().use { writer ->
            properties.store(writer, "Application Version")
        }
    }
}

tasks.bootJar {
    // jar 파일을 만들기 전에 파일 생성
    dependsOn("writeVersion")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()

    // for Spring AI. See: https://docs.spring.io/spring-ai/reference/getting-started.html#artifact-repositories
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven {
        name = "Central Portal Snapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }

}

dependencies {
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring AI
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0-SNAPSHOT"))
    implementation("org.springframework.ai:spring-ai-openai")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    // for testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
