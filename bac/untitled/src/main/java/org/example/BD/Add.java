package org.example.BD;

import lombok.experimental.UtilityClass;
import org.example.history.ResultValidation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@UtilityClass
public class Add {
    public boolean add(ResultValidation rv) throws SQLException {
        String sql = "INSERT INTO point(x, y, r, hit, checked_at, execution_micros) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = ConnectBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setBigDecimal(1, rv.x());
            ps.setBigDecimal(2, rv.y());
            ps.setBigDecimal(3, rv.r());
            ps.setBoolean(4, rv.hit());
            ps.setTimestamp(5, Timestamp.from(rv.time()));
            ps.setLong(6, rv.executionMicros());
            return ps.executeUpdate() == 1;
        }
    }
}
