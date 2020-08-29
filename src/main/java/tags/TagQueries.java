package tags;

import tools.ConverterJSON;
import tools.Query;

import java.sql.ResultSet;
import java.util.LinkedList;

public class TagQueries {
    public static String getTags(String riskId) {
        try {
            ResultSet result = Query.executeQuery("select * from tags where risk_id = '%s'", riskId);
            LinkedList<String> tags = Tag.tagsFromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(tags);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String getRisks(String tagName) {
        try {
            ResultSet result = Query.executeQuery("select * from tags where tag_name = '%s'", tagName);
            LinkedList<String> risks = Tag.risksFromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(risks);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String createTag(String tagName, String riskId) {
        try {
            Query.executeUpdate("insert into tags (tag_name, risk_id) values('%s', '%s')",
                    tagName, riskId);
            return "DONE";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String deleteTag(String tagName, String riskId) {
        try {
            Query.executeUpdate("delete from tags where tag_name = '%s' and risk_id = '%s'",
                    tagName, riskId);
            return "DONE";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
