plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:2.0.0"))
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.4.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}