package org.example.figure;

import java.math.BigDecimal;

public record Triangle(Point point,
                       BigDecimal x0,
                       BigDecimal y0,
                       BigDecimal x1,
                       BigDecimal y1,
                       BigDecimal x2,
                       BigDecimal y2) implements Figure {
    @Override
    public boolean contains(Point p) {
        return false;
    }
}
