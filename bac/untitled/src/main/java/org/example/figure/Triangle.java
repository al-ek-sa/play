package org.example.figure;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public record Triangle(Point a, Point b, Point c) implements Figure {

    //todo векторное произведение протестить и перепроверить
    @Override
    public boolean contains(Point p) {
        List<Integer> signs = Stream.of(
                b.minus(a).cross(p.minus(a)),
                c.minus(b).cross(p.minus(b)),
                a.minus(c).cross(p.minus(c))
        ).map(BigDecimal::signum).toList();
        return !(signs.contains(1) && signs.contains(-1));
    }
}
