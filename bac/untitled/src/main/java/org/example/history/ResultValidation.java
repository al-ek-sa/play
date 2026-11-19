package org.example.history;

import java.math.BigDecimal;
import java.time.Instant;

public record ResultValidation(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, Instant time, long executionMicros) {
}