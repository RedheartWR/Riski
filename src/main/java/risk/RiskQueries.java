package risk;

import org.apache.http.HttpStatus;
import tools.ConverterJSON;
import tools.Query;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

public class RiskQueries {

    public static String getrisksByGroup(Integer groupId) {
        try {
            ResultSet result = Query.executeQuery("select * from risks where groupId = \'%d\'", groupId);
            LinkedList<Risk> risks = Risk.fromResultSet(result);
            result.close();
            return ConverterJSON.toJSON(risks);
        } catch (Exception e) {
            throw new Exception(String.format("Exception %s; %s", HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    public static String getriskById(Integer id) {
        try {
            ResultSet result = Query.executeQuery("select * from risks where id = \'%d\'", id);
            Risk risk = new Risk(result);
            result.close();
            return ConverterJSON.toJSON(risk);
        }  catch (Exception e) {
            throw new Exception(String.format("Exception %s; %s", HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    public static String getriskByUser(String userEmail) {
        try {
            ResultSet result = Query.executeQuery("select * from risks where assigneeEmail = \'%d\'", userEmail);
            Risk risk = new Risk(result);
            result.close();
            return ConverterJSON.toJSON(risk);
        }  catch (Exception e) {
            throw new Exception(String.format("Exception %s; %s", HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    public static String createRisk(Integer id, String name, String description, String assigneeEmail,
                                    String creatorEmail, Date creationDate, Date lastUpdateDate,
                                    Double possibility, Double moneyLoss, Double timeLoss, Integer groupId,
                                    String status) {
        try {
            Query.executeUpdate("insert into risks (id, name, description, assigneeEmail, creatorEmail," +
                    " creationDate, lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status) values(\'%d\'," +
                    " \'%s\', \'%s\', \'%s\' , \'%s\' , \'%td\' , \'%td\' , \'%f\' , \'%f\' , \'%f\' , \'%d\' , \'%s\')",
                    id, name, description, assigneeEmail, creatorEmail,creationDate,
                    lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status);
            return "DONE";
        } catch (Exception e) {
            throw new Exception(String.format("Exception %s; %s", HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    public static String deleteRisk(Integer id) {
        try {
            Query.executeUpdate("delete from risks where id = \'%d\'", id);
            return "DONE";
        } catch (Exception e) {
            throw new Exception(String.format("Exception %s; %s", HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
