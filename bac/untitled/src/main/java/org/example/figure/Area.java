package org.example.figure;

import java.math.BigDecimal;
import java.util.List;

public record Area(List<Figure> figures) {
    private static final BigDecimal HALF = BigDecimal.valueOf(0.5);

    public static Area variant(BigDecimal r, BigDecimal offsetX, BigDecimal offsetY){
        BigDecimal half = r.multiply(HALF);

        Point origin = new Point(BigDecimal.ZERO.add(offsetX), BigDecimal.ZERO.add(offsetY));

        return new Area(List.of(
                new Rectangle(origin, r, r),

                new Triangle(
                        origin,
                        new Point(offsetX.add(half), offsetY),
                        new Point(offsetX, offsetY.subtract(half))
                ),

                QuarterCircle.of(origin, r, 180, 270)
        ));
    }

    public boolean contains(Point p){
        return figures().stream().anyMatch(figure -> figure.contains(p));
    }
}
