package org.example.figure;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public record Triangle(Point a, Point b, Point c) implements Figure {

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
