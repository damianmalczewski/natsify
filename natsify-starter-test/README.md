# Natsify Starter Test

Test utilities starter for Natsify-based applications. Extends `natsify-starter` with Spring Boot Test and Jackson test
support so integration tests have everything they need in one dependency. Intended for use in the `testImplementation`
configuration only.

## Dependency

```xml
<dependency>
    <groupId>io.github.malczuuu.natsify</groupId>
    <artifactId>natsify-starter-test</artifactId>
    <version>{version}</version>
    <scope>test</scope>
</dependency>
```

```kotlin
dependencies {
    testImplementation("io.github.malczuuu.natsify:natsify-starter-test:{version}")
}
```
