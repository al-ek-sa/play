package org.example.figure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuarterCircleTest {
    private static Point newPoint(BigDecimal x, BigDecimal y) {
        return new Point(x, y);
    }

    @ParameterizedTest
    @CsvSource({
            "-1,    -1,    0, 0,  2,  180, 270, true",
            "-2,    0,     0, 0,  2,  180, 270, true",
            "0,     -2,    0, 0,  2,  180, 270, true",
            "0,     0,     0, 0,  2,  180, 270, true",
            "-1.2,  -1.6,  0, 0,  2,  180, 270, true",
            "-1.5,  -1.5,  0, 0,  2,  180, 270, false",
            "-2.01, 0,     0, 0,  2,  180, 270, false",
            "1,     1,     0, 0,  2,  180, 270, false",
            "-1,    1,     0, 0,  2,  180, 270, false",
            "1,     -1,    0, 0,  2,  180, 270, false",
            "-0.01, 0.01,  0, 0,  2,  180, 270, false",
            "0.5,   0.5,   0, 0,  1,  0,   90,  true",
            "-0.5,  0.5,   0, 0,  1,  0,   90,  false",
            "1.5,   1.5,   1, 1,  1,  0,   90,  true",
            "0.5,   0.5,   1, 1,  1,  0,   90,  false",
            "0,     1,     0, 0,  2,  0,   180, true",
            "1,     0,     0, 0,  2,  0,   180, true",
            "0,     -1,    0, 0,  2,  0,   180, false",
            "-1,    1,     0, 0,  2,  0,   270, true",
            "-1,    -1,    0, 0,  2,  0,   270, true",
            "1,     -1,    0, 0,  2,  0,   270, false",
            "1,     1,     0, 0,  2,  30,  60,  true",
            "1,     0,     0, 0,  2,  30,  60,  false",
            "0,     1,     0, 0,  2,  30,  60,  false",
            "1,     -1,    0, 0,  2,  0,   360, true",
            "2,     2,     0, 0,  2,  0,   360, false",
            "1,     -1,    0, 0,  2,  270, 0,   true",
            "1,     1,     0, 0,  2,  270, 0,   false"
    })
    void contains(BigDecimal x,
                  BigDecimal y,
                  BigDecimal centerX,
                  BigDecimal centerY,
                  BigDecimal radius,
                  long minAngle,
                  long maxAngle,
                  boolean result) {
        Point point = newPoint(x, y);
        QuarterCircle circle = QuarterCircle.of(newPoint(centerX, centerY), radius, minAngle, maxAngle);

        boolean res = circle.contains(point);
        assertEquals(result, res);
    }
}