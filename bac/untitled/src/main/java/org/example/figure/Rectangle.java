package org.example.figure;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

/**
 * попадание точки в квадрат изменение было для написания относительно 4 точек квадрата.
 * @param a
 * @param b
 * @param c
 * @param d
 */
public record Rectangle(
        Point a,
        Point b,
        Point c,
        Point d
) implements Figure {

    @Override
    public boolean contains(Point p) {
        List<Integer> signs = Stream.of(
                b.minus(a).cross(p.minus(a)),
                c.minus(b).cross(p.minus(b)),
                d.minus(c).cross(p.minus(c)),
                a.minus(d).cross(p.minus(d))
        ).map(BigDecimal::signum).toList();

        return !(signs.contains(1) && signs.contains(-1));
    }
}
