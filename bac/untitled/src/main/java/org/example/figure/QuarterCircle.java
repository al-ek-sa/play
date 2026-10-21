package org.example.figure;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public record QuarterCircle(Point center,
                            BigDecimal radius,
                            Point bisector,
                            BigDecimal cosSquared,
                            boolean wide) implements Figure {

    private static final BigDecimal HALF = new BigDecimal("0.5");

    private static BigDecimal round(double value){
        return BigDecimal.valueOf(value).setScale(12, RoundingMode.HALF_EVEN).stripTrailingZeros();
    }

    private static Point unit(long degrees){
        double rad = Math.toRadians(degrees);
        return new Point(round(Math.cos(rad)), round(Math.sin(rad)));
    }

    private boolean inAngle(Point v){
        BigDecimal d= bisector.dot(v);
        BigDecimal left = d.multiply(d);
        BigDecimal right = bisector.lengthSquared().multiply(v.lengthSquared()).multiply(cosSquared);

        return wide ? d.signum() >= 0 || left.compareTo(right) <= 0
                : d.signum() >= 0 && left.compareTo(right) >= 0;
    }

    public static QuarterCircle of(Point center, BigDecimal radius, long minAngle, long maxAngle){
        long width = Math.floorMod(maxAngle - minAngle, 360);
        if(width == 0) width =360;
        Point start = unit(minAngle);
        Point end = unit(maxAngle);
        BigDecimal cosSquared = BigDecimal.ONE.add(start.dot(end)).multiply(HALF);
        Point sum = new Point(start.x().add(end.x()), start.y().add(end.y()));
        Point bisector;
        if(width < 180){
            bisector = sum;
        } else if(width > 180){
            bisector = new Point(sum.x().negate(), sum.y().negate());
        } else {
            bisector = new Point(start.y().negate(), start.x());
        }
        log.trace("sector {}°..{}° width={}° radius={} bisector={} cos²={} wide={}",
                minAngle, maxAngle, width, radius, bisector, cosSquared, width > 180);

        return new QuarterCircle(center, radius, bisector, cosSquared, width > 180);
    }

    @Override
    public boolean contains(Point point) {
        Point v = point.minus(center);
        return v.lengthSquared().compareTo(radius.multiply(radius)) <= 0 && inAngle(v);
    }
}
