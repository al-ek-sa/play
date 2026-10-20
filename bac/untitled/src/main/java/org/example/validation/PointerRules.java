package org.example.validation;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static org.example.validation.Rules.*;

@Slf4j
@UtilityClass
public class PointerRules {
    private final int MAX_LENGTH = 20;
    private static final Rule<String> NUMBER_VALID = Rules.REQUIRED.and(maxLength(MAX_LENGTH)).and(DECIMAL_FORMAT);

    public final Rule<String> X = NUMBER_VALID.and(between(FieldRange.X.getMin(), FieldRange.X.getMax(), FieldRange.X.getMessage()));
    public final Rule<String> Y = NUMBER_VALID.and(between(FieldRange.Y.getMin(), FieldRange.Y.getMax(), FieldRange.Y.getMessage()));
    public final Rule<String> R = NUMBER_VALID.and(between(FieldRange.R.getMin(), FieldRange.R.getMax(), FieldRange.R.getMessage()));
}