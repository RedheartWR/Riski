package status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Status implements Serializable {
    String name;

    public static LinkedList<Status> fromResultSet(ResultSet result){
        LinkedList<Status> statuses = new LinkedList<>();
        try {
            while (result.next()) {
                String name = result.getString("name");
                Status status = new Status(name);
                statuses.addLast(status);
            }
        } catch (SQLException ex) {
            // TODO: handle
        }
        return statuses;
    }
}
