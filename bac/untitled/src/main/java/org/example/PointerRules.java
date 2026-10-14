package org.example;
import static org.example.Rules.*;

public final class PointerRules {
    private static final Rule<String> NUMBER_VALID = Rules.REQUIRED.and(maxLength(20)).and(DECIMAL_FORMAT);

    public static final Rule<String> X = NUMBER_VALID.and(between(FieldRange.X.getMin(), FieldRange.X.getMax(), FieldRange.X.getMessage()));
    public static final Rule<String> Y = NUMBER_VALID.and(between(FieldRange.Y.getMin(), FieldRange.Y.getMax(), FieldRange.Y.getMessage()));
    public static final Rule<String> R = NUMBER_VALID.and(between(FieldRange.R.getMin(), FieldRange.R.getMax(), FieldRange.R.getMessage()));
    private PointerRules(){}
}
