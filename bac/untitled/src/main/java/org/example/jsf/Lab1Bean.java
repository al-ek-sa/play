package org.example.jsf;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Named
@ViewScoped
@Getter
@Setter
@NoArgsConstructor
public class Lab1Bean implements Serializable {

    @NotNull(message = "R обязателен")
    @DecimalMin(value = "0", message = "R должен быть положительным")
    @DecimalMax(value = "100", message = "R должен быть не больше 100")
    private BigDecimal r;

    @NotNull(message = "X обязателен")
    @DecimalMin(value = "-100", message = "X должен быть не меньше -100")
    @DecimalMax(value = "100", message = "X должен быть не больше 100")
    private BigDecimal x;

    @NotNull(message = "Y обязателен")
    @DecimalMin(value = "-100", message = "Y должен быть не меньше -100")
    @DecimalMax(value = "100", message = "Y должен быть не больше 100")
    private BigDecimal y;
}
