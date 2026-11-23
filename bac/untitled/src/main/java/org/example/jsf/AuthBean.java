package org.example.jsf;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
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
public class AuthBean implements Serializable {

    @NotNull(message = "offsetX обязателен для заполнения")
    private BigDecimal offsetX;

    @NotNull(message = "offsetY обязателен для заполнения")
    private BigDecimal offsetY;

    @NotNull(message = "rotation обязателен для заполнения")
    private BigDecimal rotation;
}