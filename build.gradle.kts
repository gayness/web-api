plugins {
    java
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.freefair.lombok") version "5.3.0"
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
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.guava:guava:30.1.1-jre")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
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