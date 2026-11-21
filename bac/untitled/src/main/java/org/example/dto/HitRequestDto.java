package org.example.dto;

import java.math.BigDecimal;

public record HitRequestDto(BigDecimal x, BigDecimal y, BigDecimal r) {
}
