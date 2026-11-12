package org.example.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum FieldRange {
    X(new BigDecimal(-100), new BigDecimal(100), Message.X_OUT_OF_RANGE),
    Y(new BigDecimal(-100), new BigDecimal(100), Message.Y_OUT_OF_RANGE),
    R(new BigDecimal(0), new BigDecimal(100), Message.R_OUT_OF_RANGE);

    private final BigDecimal min;
    private final BigDecimal max;
    private final Message message;
}