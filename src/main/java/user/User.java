package user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements Serializable {

    public String email;
    public String name;
    public String password;
    public Boolean isAHead;

    public User(ResultSet result) {
        try {
            email = result.getString("email");
            name = result.getString("name");
            password = result.getString("password");
            isAHead = result.getBoolean("isAHead");
        } catch (SQLException ex) { }
    }

    public static LinkedList<User> fromResultSet(ResultSet result) {
        LinkedList<User> users = new LinkedList<>();
        try {
            while (result.next()) {
                User user = new User(result);
                users.addLast(user);
            }
        } catch (SQLException ex) {
            return users;
        }
        return users;
    }
}
