package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class JDBCUtil {
    private static String url;
    private static String user;
    private static String password;
    private static int poolSize = 0;

    private static final Queue<Connection> connectionPool = new ArrayDeque<>();

    static {
        loadConfig();
        loadDriver();
        initPool();
    }

    private static void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
            url = props.getProperty("DB_URL");
            user = props.getProperty("DB_USER");
            password = props.getProperty("DB_PASSWORD");
            poolSize = Integer.parseInt(props.getProperty("DB_POOL_SIZE", "0"));
        } catch (IOException e) {
            System.err.println("Erreur chargement config DB depuis .env : " + e.getMessage());
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé : " + e.getMessage());
        }
    }

    private static void initPool() {
        if (poolSize > 0) {
            try {
                for (int i = 0; i < poolSize; i++) {
                    connectionPool.add(DriverManager.getConnection(url, user, password));
                }
                System.out.println("Pool initialisé avec " + poolSize + " connexions.");
            } catch (SQLException e) {
                System.err.println("Erreur init pool : " + e.getMessage());
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        if (poolSize > 0 && !connectionPool.isEmpty()) {
            return connectionPool.poll();
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            if (poolSize > 0 && connectionPool.size() < poolSize) {
                connectionPool.offer(conn);
            } else {
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
