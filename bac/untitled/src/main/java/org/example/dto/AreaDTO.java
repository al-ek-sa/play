package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AreaDTO(
        @NotNull
        BigDecimal offsetX,

        @NotNull
        BigDecimal offsetY,

        @NotNull
        @Max(360)
        @Min(-360)
        BigDecimal rotation
) {
}