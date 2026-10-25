package org.example.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointerRulesTest {

    private static Message messageOf(Outcome<String> outcome) {
        return switch (outcome) {
            case Outcome.Ok<String> _ -> null;
            case Outcome.Error<String> error -> error.message();
        };
    }

    @ParameterizedTest
    @CsvSource({
            "0,                          ",
            "-100,                       ",
            "100,                        ",
            "'1,5',                      ",
            "'',                         EMPTY",
            "abc,                        NOT_NUMBER",
            "123456789012345678901,      TOO_LONG",
            "aaaaaaaaaaaaaaaaaaaaaaaaa,  TOO_LONG",
            "100.01,                     X_OUT_OF_RANGE",
            "-101,                       X_OUT_OF_RANGE"
    })
    void x(String value, Message expected) {
        assertEquals(expected, messageOf(PointerRules.X.apply(value)));
    }

    @ParameterizedTest
    @CsvSource({
            "-100,      ",
            "'   ',     EMPTY",
            "'100,01',  Y_OUT_OF_RANGE"
    })
    void y(String value, Message expected) {
        assertEquals(expected, messageOf(PointerRules.Y.apply(value)));
    }

    @ParameterizedTest
    @CsvSource({
            "0,      ",
            "100,    ",
            "-0.01,  R_OUT_OF_RANGE",
            "-1,     R_OUT_OF_RANGE",
            "1e2,    NOT_NUMBER"
    })
    void r(String value, Message expected) {
        assertEquals(expected, messageOf(PointerRules.R.apply(value)));
    }
}