package org.example.BD;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class Clear {

    public void clear() throws SQLException {
        try(Connection connection = new ConnectBD().getConnection();
        Statement st = connection.createStatement()){
            String sql = "TRUNCATE TABLE point";
            st.executeUpdate(sql);
            log.debug("table point truncated");
        }
    }
}
