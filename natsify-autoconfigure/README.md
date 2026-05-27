# Natsify Auto-Configure

Spring Boot auto-configuration module that wires `natsify-core` into a Spring application context. Provides
`NatsProperties` for connection configuration, auto-configured optional health indicators, and optional Micrometer
metrics. Also includes Testcontainers integration for spinning up a NATS container in tests.

## Dependency

```xml
<dependency>
    <groupId>io.github.malczuuu.natsify</groupId>
    <artifactId>natsify-autoconfigure</artifactId>
    <version>{version}</version>
</dependency>
```

```kotlin
dependencies {
    implementation("io.github.malczuuu.natsify:natsify-autoconfigure:{version}")
}
```
