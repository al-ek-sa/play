package org.example.figure;

import java.math.BigDecimal;

public record Rictangle(Point point, BigDecimal minX, BigDecimal minY, BigDecimal r) implements Figure {
    @Override
    public boolean contains(Point p) {
        return minX.compareTo(point.x()) <= 0
                && maxX().compareTo(point.x()) >= 0
                && minY.compareTo(point.y()) <= 0
                && maxY().compareTo(point.y()) >= 0;
    }

    private BigDecimal maxX(){
        return minX.add(r);
    }

    private BigDecimal maxY(){
        return minY.add(r);
    }
}
