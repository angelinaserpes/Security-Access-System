import java.sql.*;

public class DBConnection {

    // Database settings come from environment variables so no password is stored in the code.
    // Example (Mac/Linux):  export DB_PASS="your-mysql-password"
    static final String URL  = env("DB_URL",  "jdbc:mysql://localhost:3306/security_db");
    static final String USER = env("DB_USER", "root");
    static final String PASS = env("DB_PASS", "");

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isEmpty()) ? fallback : value;
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
