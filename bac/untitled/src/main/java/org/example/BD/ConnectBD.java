package org.example.BD;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
@UtilityClass
public class ConnectBD {
    private final Path CONFIG = Path.of(System.getProperty("db.config", "db.properties"));
    private Properties properties;

    public Connection getConnection() throws SQLException {
        Properties config = config();
        log.trace("opening connection to {} as {}", config.getProperty("db.url"), config.getProperty("db.user"));
        return DriverManager.getConnection(
                config.getProperty("db.url"),
                config.getProperty("db.user"),
                config.getProperty("db.password")
        );
    }

    private Properties config() {
        if (properties == null) {
            properties = load();
        }
        return properties;
    }

    private Properties load() {
        try (Reader reader = Files.newBufferedReader(CONFIG, StandardCharsets.UTF_8)) {
            Properties loaded = new Properties();
            loaded.load(reader);
            require(loaded, "db.url");
            require(loaded, "db.user");
            require(loaded, "db.password");
            log.info("database config loaded from {}", CONFIG.toAbsolutePath());
            return loaded;
        } catch (IOException e) {
            throw new IllegalStateException("cannot read database config " + CONFIG.toAbsolutePath(), e);
        }
    }

    private void require(Properties loaded, String key) {
        if (loaded.getProperty(key) == null) {
            throw new IllegalStateException("missing " + key + " in " + CONFIG.toAbsolutePath());
        }
    }
}
