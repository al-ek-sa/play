package org.example.figure;

import java.math.BigDecimal;

public record QuarterCircle(Point point,
                            BigDecimal centerX,
                            BigDecimal canterY,
                            long minCorner,
                            long maxCorner) implements Figure {
    @Override
    public boolean contains(Point p) {
        return
    }
}
