package risk;

import tools.ConverterJSON;
import tools.Query;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class RiskQueries {

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static Risk getRiskById(Integer id) throws Exception {
        ResultSet result = Query.executeQuery("select * from risks where id = '%d'", id);
        result.next();
        Risk risk = new Risk(result);
        result.close();
        return risk;
    }

    public static String getRisks() throws Exception {
        try {
            ResultSet result = Query.executeQuery("select * from risks");
            LinkedList<Risk> risks = Risk.fromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(risks);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String getRiskByUser(String userEmail) throws Exception {
        try {
            ResultSet result = Query.executeQuery("select * from risks where assignee_email = '%s'", userEmail);
            LinkedList<Risk> risks = Risk.fromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(risks);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String updateRisk(Integer id, String name, String description, String assigneeEmail,
                                    String creatorEmail, Date creationDate, Date lastUpdateDate,
                                    Double possibility, Double moneyLoss, Double timeLoss, String status)
            throws Exception {
        try {
            Query.executeUpdate("update risks set name = '%s', description = '%s', assignee_email = '%s', creator_email = '%s'," +
                            " creation_date = '%s', last_update_date = '%s', possibility = '%s', money_loss = '%s', time_loss = '%s', status = '%s' where id = '%d'",
                    name, description, assigneeEmail, creatorEmail, df.format(creationDate),
                    df.format(lastUpdateDate), possibility.toString(), moneyLoss.toString(), timeLoss.toString(), status, id);
            return "DONE";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String createRisk(Integer id, String name, String description, String assigneeEmail,
                                    String creatorEmail, Date creationDate, Date lastUpdateDate,
                                    Double possibility, Double moneyLoss, Double timeLoss, String status)
            throws Exception {
        try {
            Query.executeUpdate("insert into risks (id, name, description, assignee_Email, creator_Email," +
                            " creation_date, last_update_date, possibility, money_Loss, time_Loss, status) values('%d'," +
                            " '%s', '%s', '%s' , '%s' , '%s' , '%s' , '%s' , '%s' , '%s' , '%s')",
                    id, name, description, assigneeEmail, creatorEmail, df.format(creationDate),
                    df.format(lastUpdateDate), possibility.toString(), moneyLoss.toString(), timeLoss.toString(), status);
            return "DONE";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String deleteRisk(Integer id) throws Exception {
        try {
            Query.executeUpdate("delete from risks where id = '%d'", id);
            return "DONE";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
