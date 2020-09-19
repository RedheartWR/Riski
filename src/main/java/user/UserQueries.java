package user;

import tools.ConverterJSON;
import tools.Query;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.UUID;

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
            result.next();
            User user = new User(result);
            result.close();
            return ConverterJSON.toJSON(user);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static boolean isUserExists(String email, String password) {
        try {
            ResultSet result= Query.executeQuery("select * from users where email ='" + email + "' and password = '" + password + "'");
            // TODO: check query
            if (!result.next() || result.getRow() != 1)
                return false;
            result.close();
            return true;
        } catch (Exception e) {
            return false;
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
            String token = UUID.randomUUID().toString();
            // Check if user exists in users table: if exists create token (some symbol set) and save it as parameter in users table, send this token back to frontend
            if (isUserExists(email, password)) {
                Query.executeUpdate("update users set token = '%s' where email = '%s'", token, email);
                return token;
            } else
                throw new IllegalArgumentException("Not authorized");
            // Add token to every request send from frontend, except /authorization request. Process it thru method checkAuthorization
            // Easy way: update this token on new user login (so old session is no longer valid) <- ALREADY DONE
            // Hard way: set deadline for this token, e.g. 10 minutes. But this too much pain in the ass
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isTokenValid(String token) {
        try {
            ResultSet result = Query.executeQuery("select * from users where token ='" + token + "'");
            if (!result.next())
                return false;
            result.close();
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

    public static String changePassword(String email, String oldPassword, String newPassword, String token) {
        try {
            Query.executeUpdate("update users set password = '%s' where email = '%s' and password = '%s' and token = '%s'",
                    newPassword, email, oldPassword, token);
            return "DONE";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
