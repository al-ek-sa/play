package org.example.BD;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectBD {
    private final String URL = "";
    private final String USER_NAME = "";
    private final String PASSWORD = "";

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

     public void closeConnect(Connection connection) throws SQLException {
         connection.close();
     }

}
