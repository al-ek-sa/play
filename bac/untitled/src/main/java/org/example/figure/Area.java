package org.example.figure;

import java.math.BigDecimal;
import java.util.List;

public record Area(List<Figure> figures) {

    private static final BigDecimal HALF = new BigDecimal("0.5");

    private static Point transform(Point point, BigDecimal rotation,
                                   BigDecimal offsetX, BigDecimal offsetY) {
        double angle = Math.toRadians(rotation.doubleValue());

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double x = point.x().doubleValue();
        double y = point.y().doubleValue();

        BigDecimal rotatedX = BigDecimal.valueOf(x * cos - y * sin)
                .add(offsetX);

        BigDecimal rotatedY = BigDecimal.valueOf(x * sin + y * cos)
                .add(offsetY);

        return new Point(rotatedX, rotatedY);
    }

    public static Area variant(
            BigDecimal r,
            BigDecimal offsetX,
            BigDecimal offsetY,
            BigDecimal rotation
    ) {
        BigDecimal half = r.multiply(HALF);

        Point origin = transform(
                new Point(BigDecimal.ZERO, BigDecimal.ZERO),
                rotation, offsetX, offsetY
        );

        Rectangle rectangle = new Rectangle(
                origin,
                transform(new Point(r, BigDecimal.ZERO), rotation, offsetX, offsetY),
                transform(new Point(r, r), rotation, offsetX, offsetY),
                transform(new Point(BigDecimal.ZERO, r), rotation, offsetX, offsetY)
        );

        Triangle triangle = new Triangle(
                origin,
                transform(new Point(half, BigDecimal.ZERO), rotation, offsetX, offsetY),
                transform(new Point(BigDecimal.ZERO, half.negate()), rotation, offsetX, offsetY)
        );

        QuarterCircle quarterCircle = QuarterCircle.of(
                origin,
                r,
                180 + rotation.intValue(),
                270 + rotation.intValue()
        );

        return new Area(List.of(
                rectangle,
                triangle,
                quarterCircle
        ));
    }

    public boolean contains(Point point) {
        return figures.stream().anyMatch(figure -> figure.contains(point));
    }
}