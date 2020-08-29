package user;

import tools.ConverterJSON;
import tools.Query;

import java.sql.ResultSet;
import java.util.LinkedList;

public class UserQueries {
    public static String getUsers() {
        try {
            ResultSet result = Query.executeQuery("select * from users");
            LinkedList<User> users = User.fromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(users);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String getUser(String email) {
        try {
            ResultSet result = Query.executeQuery("select * from users where email ='" + email + "'");
            User user = new User(result);
            result.close();
            return ConverterJSON.toJSON(user);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String createUser(String email, String name, String password, String isAHead) {
        try {
            Query.executeUpdate("insert into users (email, name, password, is_a_head) values('%s', '%s', '%s', '%s')",
                    email, name, password, isAHead);
            return "DONE";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String authorization(String email, String password) {
        try {
            //TODO
            return "";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String deleteUser(String email) {
        try {
            Query.executeUpdate("delete from users where email = '%s'", email);
            return "DONE";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String changePassword(String email, String oldPassword, String newPassword) {
        try {
            Query.executeUpdate("update users set password = '%s' where email = '%s' and password = '%s'",
                    newPassword, email, oldPassword);
            return "DONE";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
