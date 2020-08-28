package user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedList;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements Serializable{

    private String email;
    private String name;
    private String password;
    private Boolean isAHead;

    public static LinkedList<User> fromResultSet(ResultSet result){
        LinkedList<User> users = new LinkedList<>();
        try {
            while (result.next()) {
                String email = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                Boolean isAHead = result.getBoolean("isAHead");
                User user = new User(email, name, password, isAHead);
                users.addLast(user);
            }
        } catch (SQLException ex) {}
        return users;
    }

    public User(ResultSet result) {
        try {
            result.next();
            String email = result.getString("email");
            String name = result.getString("name");
            String password = result.getString("password");
            Boolean isAHead = result.getBoolean("isAHead");
            User user = new User(email, name, password, isAHead);
        } catch (SQLException ex) { }
    }
}
