package risk;

import tools.ConverterJSON;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

public class RiskQueries {
    public static String getrisksByGroup(Integer groupId) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "select * from risks where groupId = \'%d\'";
            String sql = String.format(formatQuery, groupId);
            ResultSet result = statement.executeQuery(sql);
            LinkedList<Risk> risks = new LinkedList<>();
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                String assigneeEmail = result.getString("assigneeEmail");
                String creatorEmail = result.getString("creatorEmail");
                Date creationDate = result.getDate("creationDate");
                Date lastUpdateDate = result.getDate("lastUpdateDate");
                Double possibility = result.getDouble("possibility");
                Double moneyLoss = result.getDouble("moneyLoss");
                Double timeLoss = result.getDouble("timeLoss");
                String status = result.getString("status");
                Risk risk = new Risk(id, name, description, assigneeEmail, creatorEmail, creationDate,
                        lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status);
                risks.addLast(risk);
            }
            result.close();
            statement.close();
            return ConverterJSON.toJSON(risks);
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String getriskById(Integer id) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "select * from risks where Id = \'%d\'";
            String sql = String.format(formatQuery, id);
            ResultSet result = statement.executeQuery(sql);
            result.next();
            String name = result.getString("name");
            String description = result.getString("description");
            String assigneeEmail = result.getString("assigneeEmail");
            String creatorEmail = result.getString("creatorEmail");
            Date creationDate = result.getDate("creationDate");
            Date lastUpdateDate = result.getDate("lastUpdateDate");
            Double possibility = result.getDouble("possibility");
            Double moneyLoss = result.getDouble("moneyLoss");
            Double timeLoss = result.getDouble("timeLoss");
            Integer groupId = result.getInt("groupId");
            String status = result.getString("status");
            Risk risk = new Risk(id, name, description, assigneeEmail, creatorEmail, creationDate,
                    lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status);
            result.close();
            statement.close();
            return ConverterJSON.toJSON(risk);
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String getriskByUser(String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "select * from risks where assigneeEmail = \'%s\'";
            String sql = String.format(formatQuery, userEmail);
            ResultSet result = statement.executeQuery(sql);
            LinkedList<Risk> risks = new LinkedList<>();
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                String assigneeEmail = result.getString("assigneeEmail");
                String creatorEmail = result.getString("creatorEmail");
                Date creationDate = result.getDate("creationDate");
                Date lastUpdateDate = result.getDate("lastUpdateDate");
                Double possibility = result.getDouble("possibility");
                Double moneyLoss = result.getDouble("moneyLoss");
                Double timeLoss = result.getDouble("timeLoss");
                Integer groupId = result.getInt("groupId");
                String status = result.getString("status");
                Risk risk = new Risk(id, name, description, assigneeEmail, creatorEmail, creationDate,
                        lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status);
                risks.addLast(risk);
            }
            result.close();
            statement.close();
            return ConverterJSON.toJSON(risks);
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String createRisk(Integer id, String name, String description, String assigneeEmail,
                                    String creatorEmail, Date creationDate, Date lastUpdateDate,
                                    Double possibility, Double moneyLoss, Double timeLoss, Integer groupId,
                                    String status) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "insert into risks (id, name, description, assigneeEmail, creatorEmail," +
                    " creationDate, lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status) values(\'%d\'," +
                    " \'%s\', \'%s\', \'%s\' , \'%s\' , \'%td\' , \'%td\' , \'%f\' , \'%f\' , \'%f\' , \'%d\' , \'%s\')";
            String sql = String.format(formatQuery, id, name, description, assigneeEmail, creatorEmail,creationDate,
                    lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status);
            statement.executeUpdate(sql);
            statement.close();
            return "DONE";
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }

    public static String deleteRisk(Integer id) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",
                    "gmt", "123abc");
            Statement statement = connection.createStatement();
            String formatQuery = "delete from risks where email = \'%d\'";
            String sql = String.format(formatQuery, id);
            statement.executeUpdate(sql);
            statement.close();
            return "DONE";
        } catch (SQLException ex) {
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException e) {
            return e.toString();
        }
    }
}
