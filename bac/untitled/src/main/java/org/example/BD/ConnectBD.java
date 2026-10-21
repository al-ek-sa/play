package org.example.BD;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@UtilityClass
public class ConnectBD {
    private final String URL = "";
    private final String USER_NAME = "";
    private final String PASSWORD = "";

    public Connection getConnection() throws SQLException{
        log.trace("opening connection to {} as {}", URL, USER_NAME);
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
