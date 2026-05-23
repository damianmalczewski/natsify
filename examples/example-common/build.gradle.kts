plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(project(":natsify-starter"))

    testImplementation(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.platform.launcher)
}
