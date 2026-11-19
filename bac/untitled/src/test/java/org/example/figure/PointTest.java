package org.example.figure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class PointTest {
    private static Point newPoint(BigDecimal x, BigDecimal y){
        return new Point(x, y);
    }

    @ParameterizedTest
    @CsvSource({
            "20,  1,    19,   4,    1,    -3",
            "-10, -3,   -13,  2,    3,    -5",
            "0,   31,   31,   0,    -31,  31",
            "5,   5,    5,    5,    0,    0",
            "1.5, 2.0,  0.25, 0.5,  1.25, 1.5"
    })
    void minus(BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1, BigDecimal resultX, BigDecimal resultY){
        Point p0 = newPoint(x0, y0);
        Point p1 = newPoint(x1, y1);
        Point result = newPoint(resultX, resultY);

        Point res = p0.minus(p1);
        assertEquals(res, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1,   2,  3,   4,    11",
            "1,   0,  0,   1,    0",
            "2,   3,  -2,  -3,   -13",
            "-5,  4,  2,   6,    14",
            "0,   0,  7,   -3,   0",
            "0.5, 2,  4,   0.25, 2.5"
    })
    void dot(BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1, BigDecimal result){
        Point p0 = newPoint(x0, y0);
        Point p1 = newPoint(x1, y1);

        BigDecimal res = p0.dot(p1);
        assertEquals(0, res.compareTo(result) );
    }

    @ParameterizedTest
    @CsvSource({
            "1,   0,  0,   1,  1",
            "0,   1,  1,   0,  -1",
            "2,   3,  4,   5,  -2",
            "2,   4,  1,   2,  0",
            "-3,  2,  5,   1,  -13",
            "3,   -1, -2,  4,  10",
            "1.5, 0,  0,   2,  3"
    })
    void cross(BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1, BigDecimal result){
        Point p0 = newPoint(x0, y0);
        Point p1 = newPoint(x1, y1);

        BigDecimal res = p0.cross(p1);
        assertEquals(0, res.compareTo(result));
    }

    @ParameterizedTest
    @CsvSource(
            {
                    "3,  4,  25",
                    "0,  0,  0",
                    "-3, -4, 25",
                    "1.2, -1.6, 4"
            }
    )
    void lengthSquared(BigDecimal x, BigDecimal y, BigDecimal result){
        Point p = newPoint(x, y);

        BigDecimal res = p.lengthSquared();
        assertEquals(0, res.compareTo(result));
    }
}
