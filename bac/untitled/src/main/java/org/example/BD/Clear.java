package org.example.BD;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@UtilityClass
public class Clear {
    private final String sql = "TRUNCATE point";

    public void clear() throws SQLException {
        try(Connection connection = ConnectBD.getConnection();
        Statement st = connection.createStatement()){
            st.executeUpdate(sql);
            log.debug("table point truncated");
        }
    }
}
