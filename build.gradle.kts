plugins {
  id("org.springframework.boot") version "3.3.1"
  id("io.spring.dependency-management") version "1.1.5"
  kotlin("jvm") version "1.9.24"
  kotlin("plugin.jpa") version "1.9.24"
  kotlin("plugin.spring") version "1.9.24"
  kotlin("plugin.noarg") version "1.9.24"
}

group = "majority.elite"

version = "0.0.1-SNAPSHOT"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

configurations { compileOnly { extendsFrom(configurations.annotationProcessor.get()) } }

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-websocket")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.springframework.security:spring-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  runtimeOnly("org.postgresql:postgresql")
  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
  implementation("io.lettuce:lettuce-core:6.4.0.RELEASE")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
  implementation("org.apache.commons:commons-pool2")
}

kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") } }

tasks.withType<Test> { useJUnitPlatform() }
