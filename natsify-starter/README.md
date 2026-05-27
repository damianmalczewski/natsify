# Natsify Starter

Convenience starter for adding Natsify to a Spring Boot application. Pulls in `natsify-autoconfigure`, the Spring Boot
starter, and Jackson in a single dependency. Add this to your project instead of depending on core and autoconfigure
separately.

## Dependency

```xml
<dependency>
    <groupId>io.github.malczuuu.natsify</groupId>
    <artifactId>natsify-starter</artifactId>
    <version>{version}</version>
</dependency>
```

```kotlin
dependencies {
    implementation("io.github.malczuuu.natsify:natsify-starter:{version}")
}
```
