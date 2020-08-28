package status;

import tools.ConverterJSON;
import tools.Query;

import java.sql.ResultSet;
import java.util.LinkedList;

public class StatusQueries {
    public static String getUsers() {
        try {
            ResultSet result = Query.executeQuery("select * from statuses");
            LinkedList<Status> statuses = Status.fromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(statuses);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
