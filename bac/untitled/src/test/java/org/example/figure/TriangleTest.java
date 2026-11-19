package org.example.figure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class TriangleTest {
    private static Point newPoint(BigDecimal x, BigDecimal y) {
        return new Point(x, y);
    }

    private static Triangle newTriangle(Point one, Point two, Point three){
        return new Triangle(one, two, three);
    }

    @ParameterizedTest
    @CsvSource({
            "1,    1,     0, 0,   4, 0,   0, 4,   true",
            "0,    0,     0, 0,   4, 0,   0, 4,   true",
            "2,    0,     0, 0,   4, 0,   0, 4,   true",
            "2,    2,     0, 0,   4, 0,   0, 4,   true",
            "1.5,  2.5,   0, 0,   4, 0,   0, 4,   true",
            "3,    3,     0, 0,   4, 0,   0, 4,   false",
            "-1,   1,     0, 0,   4, 0,   0, 4,   false",
            "1,    -0.01, 0, 0,   4, 0,   0, 4,   false",
            "1,    1,     0, 0,   0, 4,   4, 0,   true",
            "3,    3,     0, 0,   0, 4,   4, 0,   false",
            "1,    -1,    0, 0,   2, 0,   0, -2,  true",
            "0.5,  -0.5,  0, 0,   2, 0,   0, -2,  true",
            "1.5,  -1,    0, 0,   2, 0,   0, -2,  false",
            "1,    1,     0, 0,   2, 0,   0, -2,  false",
            "1,    1,     -2, -1, 3, 0,   1, 4,   true",
            "0,    3,     -2, -1, 3, 0,   1, 4,   false"
    })
    void contains(BigDecimal x,
                  BigDecimal y,
                  BigDecimal x1,
                  BigDecimal y1,
                  BigDecimal x2,
                  BigDecimal y2,
                  BigDecimal x3,
                  BigDecimal y3,
                  boolean result){
        Point point = newPoint(x, y);
        Triangle triangle = newTriangle(
                                        newPoint(x1, y1),
                                        newPoint(x2, y2),
                                        newPoint(x3, y3));
        boolean res = triangle.contains(point);
        assertEquals(res, result);
    }
}
