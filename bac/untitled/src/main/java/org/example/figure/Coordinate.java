package org.example.figure;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public record Coordinate(BigDecimal x, BigDecimal y, BigDecimal r) {
    public Point point(){
        return new Point(x, y);
    }
}
