package org.example.validation;

import org.example.figure.Coordinate;
import org.example.figure.FieldError;
import org.example.figure.Valid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class PointValidatorTest {

    private static Map<String, String> params(String x, String y, String r) {
        Map<String, String> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        params.put("r", r);
        return params;
    }

    private static List<FieldError> errorsOf(String x, String y, String r) {
        Valid<Coordinate> result = PointValidator.validate(params(x, y, r));
        Valid.Failure<Coordinate> failure = assertInstanceOf(Valid.Failure.class, result);
        return failure.errors();
    }

    @ParameterizedTest
    @CsvSource({
            "1,       2,       3,      1,      2,     3",
            "'1,5',   -2.25,   0.5,    1.5,    -2.25, 0.5",
            "-100,    100,     0,      -100,   100,   0",
            "100,     -100,    100,    100,    -100,  100",
            "' 7 ',   -0,      4,      7,      0,     4",
            "0.0001,  '3,75',  99.99,  0.0001, 3.75,  99.99"
    })
    void validInput(String x, String y, String r, BigDecimal resultX, BigDecimal resultY, BigDecimal resultR) {
        Valid<Coordinate> result = PointValidator.validate(params(x, y, r));

        Valid.Success<Coordinate> success = assertInstanceOf(Valid.Success.class, result);
        assertEquals(0, resultX.compareTo(success.value().x()));
        assertEquals(0, resultY.compareTo(success.value().y()));
        assertEquals(0, resultR.compareTo(success.value().r()));
    }

    @ParameterizedTest
    @CsvSource({
            ",        2,  3,      x,  EMPTY",
            "'',      2,  3,      x,  EMPTY",
            "'   ',   2,  3,      x,  EMPTY",
            "1,        ,  3,      y,  EMPTY",
            "1,       2,   ,      r,  EMPTY",

            "abc,     2,  3,      x,  NOT_NUMBER",
            "1.,      2,  3,      x,  NOT_NUMBER",
            ".5,      2,  3,      x,  NOT_NUMBER",
            "1e2,     2,  3,      x,  NOT_NUMBER",
            "1.2.3,   2,  3,      x,  NOT_NUMBER",
            "--1,     2,  3,      x,  NOT_NUMBER",
            "+1,      2,  3,      x,  NOT_NUMBER",
            "1,       2,  0x10,   r,  NOT_NUMBER",

            "123456789012345678901,  2,  3,                      x,  TOO_LONG",
            "1,                      2,  1.0000000000000000000,  r,  TOO_LONG",

            "100.01,  2,    3,       x,  X_OUT_OF_RANGE",
            "-100.01, 2,    3,       x,  X_OUT_OF_RANGE",
            "1,       101,  3,       y,  Y_OUT_OF_RANGE",
            "1,       2,    -0.01,   r,  R_OUT_OF_RANGE",
            "1,       2,    '100,5', r,  R_OUT_OF_RANGE"
    })
    void oneFieldError(String x, String y, String r, String field, Message message) {
        assertEquals(List.of(new FieldError(field, message)), errorsOf(x, y, r));
    }

    @Test
    void allEmpty() {
        assertEquals(List.of(
                new FieldError("x", Message.EMPTY),
                new FieldError("y", Message.EMPTY),
                new FieldError("r", Message.EMPTY)
        ), errorsOf(null, null, null));
    }

    @Test
    void onlyTopPriorityIsReturned() {
        assertEquals(List.of(new FieldError("x", Message.EMPTY), new FieldError("y", Message.NOT_NUMBER), new FieldError("r", Message.R_OUT_OF_RANGE)), errorsOf("", "abc", "500"));
    }

    @Test
    void notNumberBeatsOutOfRange() {
        assertEquals(List.of(new FieldError("x", Message.NOT_NUMBER), new FieldError("y", Message.Y_OUT_OF_RANGE), new FieldError("r", Message.R_OUT_OF_RANGE)), errorsOf("abc", "200", "500"));
    }

    @Test
    void notNumberBeatsTooLong() {
        assertEquals(List.of(new FieldError("x", Message.TOO_LONG), new FieldError("y", Message.NOT_NUMBER)), errorsOf("123456789012345678901", "abc", "3"));
    }

    @Test
    void sameLevelErrorsComeTogether() {
        assertEquals(List.of(
                new FieldError("y", Message.Y_OUT_OF_RANGE),
                new FieldError("r", Message.R_OUT_OF_RANGE)
        ), errorsOf("1", "200", "-1"));
    }
}