package org.example.figure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AreaTest {
    private static Point newPoint(BigDecimal x, BigDecimal y) {
        return new Point(x, y);
    }

    @ParameterizedTest
    @CsvSource({
            "1,     3,     4,  true",
            "2,     4,     4,  true",
            "0,     4,     4,  true",
            "2.01,  1,     4,  false",
            "1,     4.01,  4,  false",
            "2,     -2,    4,  true",
            "2.4,   -3.2,  4,  true",
            "4,     0,     4,  true",
            "0,     -4,    4,  true",
            "3,     -3,    4,  false",
            "4.01,  0,     4,  false",
            "-1,    -3,    4,  true",
            "-2,    -4,    4,  true",
            "-2,    0,     4,  true",
            "-2.01, -1,    4,  false",
            "-1,    -4.01, 4,  false",
            "-1,    1,     4,  false",
            "-0.01, 0.01,  4,  false",
            "0,     0,     4,  true",
            "0.5,   1.5,   2,  true",
            "1.01,  0.5,   2,  false",
            "1.2,   -1.6,  2,  true",
            "1.5,   -1.5,  2,  false",
            "-1,    -2,    2,  true",
            "-1.01, -1,    2,  false",
            "0.75,  2.5,   3,  true",
            "1.8,   -2.4,  3,  true",
            "-1.5,  -3,    3,  true",
            "0,     0,     0,  true"
    })
    void contains(BigDecimal x, BigDecimal y, BigDecimal r, boolean result) {
        Area area = Area.variant(r);

        boolean res = area.contains(newPoint(x, y));
        assertEquals(result, res);
    }
}