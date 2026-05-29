package org.example.natspring.listenerdeadletter;

import java.util.List;
import java.util.Map;

public record DeadLetteredMessage(String body, Map<String, List<String>> headers) {}
