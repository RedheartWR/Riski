package status;

import tools.ConverterJSON;
import tools.Query;

import java.sql.ResultSet;
import java.util.LinkedList;

public class StatusQueries {
    public static String getUsers() throws Exception {
        try {
            ResultSet result = Query.executeQuery("select * from statuses");
            LinkedList<Status> statuses = Status.fromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(statuses);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
