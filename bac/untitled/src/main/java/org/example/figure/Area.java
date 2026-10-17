package org.example.figure;

import java.math.BigDecimal;
import java.util.List;

public record Area(List<Figure> figures) {
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private static Area variant(BigDecimal r){
        BigDecimal half = r.divide(TWO);

        Point origin = new Point(BigDecimal.ZERO, BigDecimal.ZERO);

        return new Area(List.of(
                new Rectangle(origin, half, r),
                new Triangle(origin, new Point(half, BigDecimal.ZERO), new Point(BigDecimal.ZERO, BigDecimal.ZERO.subtract(r))),
                QuarterCircle.of(origin, half, 180, 270),

                new Rectangle(new Point(BigDecimal.ZERO.subtract(half), BigDecimal.ZERO.subtract(r)), half, r),
                new Triangle(origin, new Point(half, BigDecimal.ZERO), new Point(BigDecimal.ZERO, r)),
                QuarterCircle.of(origin, r, 270, 0)
        ));
    }

    public boolean contains(Point p){
        return figures().stream().anyMatch(figure -> figure.contains(p));
    }
}
