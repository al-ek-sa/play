package org.example.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record HitRequestDto(

        @NotNull(message = "X обязателен к заполнению")
        @DecimalMax(value = "100", message = "X должен быть не больше 100")
        @DecimalMin(value = "-100", message = "X должен быть не меньше -100")
        BigDecimal x,

        @NotNull(message = "Y обязателен к заполнению")
        @DecimalMax(value = "100", message = "Y должен быть не больше 100")
        @DecimalMin(value = "-100", message = "Y должен быть не меньше -100")
        BigDecimal y,

        @NotNull(message = "R обязателен к заполнению")
        @DecimalMax(value = "100", message = "R должен быть не больше 100")
        @DecimalMin(value = "0", message = "R должен быть не меньше 0")
        BigDecimal r
) {
}