package org.example.figure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RectangleTest {
    private static Point newPoint(BigDecimal x, BigDecimal y) {
        return new Point(x, y);
    }

    @ParameterizedTest
    @CsvSource({
            "1,     3,     0,  0,   2,   4,   true",
            "0,     0,     0,  0,   2,   4,   true",
            "2,     4,     0,  0,   2,   4,   true",
            "2,     0,     0,  0,   2,   4,   true",
            "0,     4,     0,  0,   2,   4,   true",
            "1,     0,     0,  0,   2,   4,   true",
            "2.01,  1,     0,  0,   2,   4,   false",
            "1,     4.01,  0,  0,   2,   4,   false",
            "-0.01, 1,     0,  0,   2,   4,   false",
            "1,     -0.01, 0,  0,   2,   4,   false",
            "3,     5,     0,  0,   2,   4,   false",
            "-1,    -1,    -3, -2,  4,   3,   true",
            "1,     1,     -3, -2,  4,   3,   true",
            "1.5,   0,     -3, -2,  4,   3,   false",
            "0.25,  0.5,   0,  0,   0.5, 0.5, true",
            "0.75,  0.25,  0,  0,   0.5, 0.5, false"
    })
    void contains(BigDecimal x,
                  BigDecimal y,
                  BigDecimal startX,
                  BigDecimal startY,
                  BigDecimal width,
                  BigDecimal height,
                  boolean result) {
        Point point = newPoint(x, y);
        Rectangle rectangle = new Rectangle(newPoint(startX, startY), width, height);

        boolean res = rectangle.contains(point);
        assertEquals(result, res);
    }
}