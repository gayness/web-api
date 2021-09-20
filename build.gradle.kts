plugins {
    java
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.freefair.lombok") version "6.1.0"
    id("org.asciidoctor.convert") version "1.5.8"
}

group = "pink.zak.api"
version = "0.0.1-SNAPSHOT"

val snippetsDir by extra { file("build/generated-snippets") }

java {
    sourceCompatibility = JavaVersion.VERSION_11
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("se.michaelthelin.spotify:spotify-web-api-java:6.5.4")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.jetbrains:annotations:22.0.0")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

ext {
    set("snippetsDir", snippetsDir)
}

tasks {
    test {
        useJUnitPlatform()
    }
    val testTask = withType<Test> {
        outputs.dir(snippetsDir)
    }
    withType<JavaCompile> {
        inputs.files(processResources)
    }
    withType<org.asciidoctor.gradle.AsciidoctorTask> {
        dependsOn(testTask)
        inputs.dir(snippetsDir)
    }
}