package org.example.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesTest {

    private static Message messageOf(Outcome<String> outcome) {
        return switch (outcome) {
            case Outcome.Ok<String> ok -> null;
            case Outcome.Error<String> error -> error.message();
        };
    }

    @ParameterizedTest
    @CsvSource({
            "1,      ",
            "abc,    ",
            "' 0 ',  ",
            "'',     EMPTY",
            "'   ',  EMPTY",
            ",       EMPTY"
    })
    void required(String value, Message expected) {
        assertEquals(expected, messageOf(Rules.REQUIRED.apply(value)));
    }

    @ParameterizedTest
    @CsvSource({
            "1,        ",
            "-1,       ",
            "0,        ",
            "1.5,      ",
            "'1,5',    ",
            "' 2 ',    ",
            "-0.25,    ",
            "007,      ",
            "abc,      NOT_NUMBER",
            "1.,       NOT_NUMBER",
            "'1,',     NOT_NUMBER",
            ".5,       NOT_NUMBER",
            "1e2,      NOT_NUMBER",
            "+1,       NOT_NUMBER",
            "'1,2,3',  NOT_NUMBER",
            "--1,      NOT_NUMBER",
            "'1 2',    NOT_NUMBER",
            "'',       NOT_NUMBER"
    })
    void decimalFormat(String value, Message expected) {
        assertEquals(expected, messageOf(Rules.DECIMAL_FORMAT.apply(value)));
    }

    @ParameterizedTest
    @CsvSource({
            "12345,     ",
            "'',        ",
            "'  1  ',   ",
            "-1.25,     ",
            "123456,    TOO_LONG",
            "-1.255,    TOO_LONG"
    })
    void maxLength(String value, Message expected) {
        assertEquals(expected, messageOf(Rules.maxLength(5).apply(value)));
    }

    @ParameterizedTest
    @CsvSource({
            "-1,       ",
            "1,        ",
            "0,        ",
            "'0,5',    ",
            "-0.999,   ",
            "' 1 ',    ",
            "1.000,    ",
            "1.01,     X_OUT_OF_RANGE",
            "-1.01,    X_OUT_OF_RANGE",
            "100,      X_OUT_OF_RANGE",
            "'1,001',  X_OUT_OF_RANGE"
    })
    void between(String value, Message expected) {
        Rule<String> rule = Rules.between(new BigDecimal("-1"), new BigDecimal("1"), Message.X_OUT_OF_RANGE);

        assertEquals(expected, messageOf(rule.apply(value)));
    }

    @ParameterizedTest
    @CsvSource({
            "'1,5',   1.5",
            "' 2 ',   2",
            "-0.25,   -0.25",
            "'-3,0',  -3"
    })
    void toDecimal(String value, BigDecimal expected) {
        assertEquals(0, expected.compareTo(Rules.toDecimal(value)));
    }
}