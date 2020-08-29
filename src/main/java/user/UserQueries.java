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
//            result.next(); // TODO: check if necessary
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
            // TODO: check if user exists in users table: if exists create token (some symbol set, e.g. 123abcZXY) and save it as parameter in users table, send this token back to frontend
            // Add token to every request send from frontend, except /authorization request. Process it thru method checkAuthorization
            // Easy way: update this token on new user login (so old session is no longer valid)
            // Hard way: set deadline for this token, e.g. 10 minutes. But this to much pain in the ass
            return "123abcZXY";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static Boolean checkAuthorization(String token) {
        try {
            // TODO: check if token exists in users table; Exists -> true, else -> false
            return true;
        } catch (Exception e) {
            return false;
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
