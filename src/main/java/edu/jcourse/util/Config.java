package edu.jcourse.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class Config {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_DRIVER = "db.driver";
    public static final String DB_POOL_SIZE = "db.pool.size";
    public static final String IMAGE_BASE_URL = "image.base.url";

    private static final Properties PROPERTIES = new Properties();

    @SneakyThrows
    public static String getProperty(String name) {
        if (!PROPERTIES.isEmpty()) {
            return System.getenv().getOrDefault(name, PROPERTIES.getProperty(name));
        }

        try (InputStream resource = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resource);
        }
        return System.getenv().getOrDefault(name, PROPERTIES.getProperty(name));
    }
}