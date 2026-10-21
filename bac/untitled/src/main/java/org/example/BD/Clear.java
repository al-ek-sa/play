package org.example.BD;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@UtilityClass
public class Clear {
    private final String sql = "TRUNCATE point";

    public void clear() throws SQLException {
        try(Connection connection = ConnectBD.getConnection();
        Statement st = connection.createStatement()){
            st.executeUpdate(sql);
        }
    }
}
