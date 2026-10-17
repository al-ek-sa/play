package org.example.history;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResultValidation(BigDecimal x, BigDecimal y, BigDecimal r, boolean result, LocalDateTime time) {
}
