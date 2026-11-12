package org.example.figure;

import java.math.BigDecimal;

public record Rectangle(Point pointStart, BigDecimal width, BigDecimal height) implements Figure {
    @Override
    public boolean contains(Point point) {
        return pointStart.x().compareTo(point.x()) <= 0
                && maxX().compareTo(point.x()) >= 0
                && pointStart.y().compareTo(point.y()) <= 0
                && maxY().compareTo(point.y()) >= 0;
    }

    private BigDecimal maxX(){
        return pointStart.x().add(width);
    }

    private BigDecimal maxY(){
        return pointStart.y().add(height);
    }
}
