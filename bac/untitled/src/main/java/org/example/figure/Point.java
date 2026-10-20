package org.example.figure;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public record Point(BigDecimal x, BigDecimal y) {
    public Point minus(Point other){
        return new Point(x.subtract(other.x), y.subtract(other.y));
    }

    public BigDecimal dot(Point other){
        return x.multiply(other.x).add(y.multiply(other.y));
    }

    public BigDecimal cross(Point other){
        return x.multiply(other.y).subtract(y.multiply(other.x));
    }

    public BigDecimal lengthSquared(){
        return dot(this);
    }
}
