package user;

import tools.ConverterJSON;

import java.sql.*;
import java.util.LinkedList;

public class UserQueries {
    public static String getUsers() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String sql = "select * from users";
            ResultSet result = statement.executeQuery(sql);
            LinkedList<User> users = new LinkedList<>();
            while (result.next()) {
                String email = result.getString("email");
                String name = result.getString("name");
                Boolean isAHead = result.getBoolean("is_a_head");
                User user = new User(email, name, "", isAHead);
                users.addLast(user);
            }
            result.close();
            statement.close();
            return ConverterJSON.toJSON(users);
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String getUser(String email) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String sql = "select * from users where email ='" + email + "'";
            ResultSet result = statement.executeQuery(sql);
                result.next();
                String name = result.getString("name");
                Boolean isAHead = result.getBoolean("is_a_head");
                User user = new User(email, name, "", isAHead);
                result.close();
                statement.close();
                return ConverterJSON.toJSON(user);
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String createUser(String email, String name, String password, String isAHead) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "insert into users (email, name, password, is_a_head) values(\'%s\', \'%s\', \'%s\', \'%s\')";
            String sql = String.format(formatQuery, email, name, password, isAHead);
            statement.executeUpdate(sql);
            statement.close();
            return "DONE";
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String authorization(String email, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "select * from users where email =\'%s\'";
            String sql = String.format(formatQuery, email);
            ResultSet result = statement.executeQuery(sql);
            result.next();
            String res = result.getString("password") == password ? "Y" : "N";
            statement.close();
            return res;
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String deleteUser(String email) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "delete from users where email = \'%s\'";
            String sql = String.format(formatQuery, email);
            statement.executeUpdate(sql);
            statement.close();
            return "DONE";
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }
}
