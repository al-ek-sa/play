package org.example.figure;

import java.math.BigDecimal;

public record Coordinate(BigDecimal x, BigDecimal y) {
    public Point point(){
        return new Point(x, y);
    }
}
