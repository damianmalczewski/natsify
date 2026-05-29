package org.example.natspring.jetstreamdeadletter;

import java.util.List;
import java.util.Map;

public record DeadLetteredMessage(String body, Map<String, List<String>> headers) {}
