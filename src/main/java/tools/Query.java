package tools;

import java.sql.*;

public class Query {
    private static Connection connection = createConnection();

    private static Connection createConnection()  {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static ResultSet executeQuery(String formatQuery, Object... args) throws Exception {
        Statement statement = connection.createStatement();
        String sql = String.format(formatQuery, args);
        ResultSet result = statement.executeQuery(sql);
        statement.close();
        return result;
    }

    public static void executeUpdate(String formatQuery, Object... args) throws Exception {
        Statement statement = connection.createStatement();
        String sql = String.format(formatQuery, args);
        statement.executeUpdate(sql);
        statement.close();
        return;
    }
}