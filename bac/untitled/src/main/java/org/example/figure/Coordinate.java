package org.example.figure;

import java.math.BigDecimal;

public record Coordinate(BigDecimal x, BigDecimal y, BigDecimal r) {
    public Point point(){
        return new Point(x, y);
    }
}
