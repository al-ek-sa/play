package org.example.BD;

import lombok.extern.slf4j.Slf4j;
import org.example.history.ResultValidation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Slf4j
public class Add {
    public boolean add(ResultValidation rv) throws SQLException {
        String sql = "INSERT INTO point(x, y, r, hit, checked_at, execution_micros) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = new ConnectBD().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setBigDecimal(1, rv.x());
            ps.setBigDecimal(2, rv.y());
            ps.setBigDecimal(3, rv.r());
            ps.setBoolean(4, rv.hit());
            ps.setTimestamp(5, Timestamp.from(rv.time()));
            ps.setLong(6, rv.executionMicros());

            int rows = ps.executeUpdate();
            if (rows != 1) {
                log.warn("insert affected {} rows, expected 1: {}", rows, rv);
                return false;
            }
            log.debug("saved x={} y={} r={} hit={}", rv.x(), rv.y(), rv.r(), rv.hit());
            return true;
        }
    }
}
