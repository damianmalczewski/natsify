import com.diffplug.spotless.LineEnding

plugins {
    id("com.diffplug.spotless")
    id("internal.java-convention")
    id("internal.jacoco-convention")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":natspring-starter"))
    implementation(libs.spring.boot.starter.data.mongodb)
    implementation(libs.spring.boot.starter.webmvc)

    testImplementation(project(":natspring-starter-test"))
    testImplementation(libs.spring.boot.starter.data.mongodb.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.mongodb)
    testImplementation(libs.testcontainers.nats)

    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Jar>().configureEach {
    if (name != "bootJar") {
        enabled = false
    }
}

spotless {
    java {
        target("src/**/*.java")
        targetExclude("build/**")

        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("yaml") {
        target("src/**/*.yml", "src/**/*.yaml")
        targetExclude("build/**")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}
