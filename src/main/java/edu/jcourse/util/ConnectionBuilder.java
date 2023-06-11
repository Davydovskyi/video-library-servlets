package edu.jcourse.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@UtilityClass
public class ConnectionBuilder {

    private static final int DEFAULT_POOL_SIZE = 10;
    private static List<Connection> sourcePool;
    private static BlockingQueue<Connection> pool;
    private static boolean poolOpened;

    static {
        loadDriver();
        initConnectionPool();
    }

    private static void initConnectionPool() {
        String poolSize = Config.getProperty(Config.DB_POOL_SIZE);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourcePool = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Connection connection = openConnection();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionBuilder.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourcePool.add(connection);
        }
        poolOpened = true;
    }

    @SneakyThrows
    public static Connection getConnection() {
        return pool.take();
    }

    @SneakyThrows
    private static Connection openConnection() {
        return DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD)
        );
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName(Config.getProperty(Config.DB_DRIVER));
    }

    @SneakyThrows
    public static void closePool() {
        for (Connection connection : sourcePool) {
            connection.close();
        }
        poolOpened = false;
    }

    public static boolean isPoolOpened() {
        return poolOpened;
    }
}