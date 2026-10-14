package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldRange {
    X("-100", "100", Message.X_OUT_OF_RANGE),
    Y("-100", "100", Message.Y_OUT_OF_RANGE),
    R("0", "100", Message.R_OUT_OF_RANGE);

    private final String min;
    private final String max;
    private final Message message;
}
