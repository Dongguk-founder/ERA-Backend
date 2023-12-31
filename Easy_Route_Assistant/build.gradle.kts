plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.20"
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

group = "com.founder"
version = "0.0.1-SNAPSHOT"


java {
    sourceCompatibility = JavaVersion.VERSION_17
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
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    //WebClient 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Redis cache
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    //JSON 파싱 라이브러리
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("org.json:json:20231013")
    //m1 노트북
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")
    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.sun.xml.bind:jaxb-impl:4.0.1")
    implementation("com.sun.xml.bind:jaxb-core:4.0.1")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    // implementation("javax.xml.bind:jaxb-api:2.3.1")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp")
    // SpringBoot Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    // apache poi
    implementation("org.apache.poi:poi:4.1.2")
    implementation("org.apache.poi:poi-ooxml:4.1.2");

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.config:spring-config-test")
    //implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}