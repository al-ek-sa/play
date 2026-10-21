package org.example.BD;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.history.ResultValidation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class FindAll {
    private final String SQL =
            "SELECT x, y, r, hit, checked_at, execution_micros FROM point ORDER BY id DESC";

    public List<ResultValidation> findAll() throws SQLException {
        try (Connection conn = ConnectBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            List<ResultValidation> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new ResultValidation(
                        rs.getBigDecimal("x"),
                        rs.getBigDecimal("y"),
                        rs.getBigDecimal("r"),
                        rs.getBoolean("hit"),
                        rs.getTimestamp("checked_at").toInstant(),
                        rs.getLong("execution_micros")
                ));
            }
            log.debug("loaded {} results", results.size());
            return List.copyOf(results);
        }
    }
}