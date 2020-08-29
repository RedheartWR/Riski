package risk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Risk implements Serializable {
    Integer id;
    String name;
    String description;
    String assigneeEmail;
    String creatorEmail;
    Date creationDate;
    Date lastUpdateDate;
    Double possibility;
    Double moneyLoss;
    Double timeLoss;
    String status;

    public Risk(ResultSet result) {
        try {
            result.next();
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
                    lastUpdateDate, possibility, moneyLoss, timeLoss, status);
        } catch (SQLException ex) {
        }
    }

    public static LinkedList<Risk> fromResultSet(ResultSet result) {
        LinkedList<Risk> risks = new LinkedList<>();
        try {
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
                        lastUpdateDate, possibility, moneyLoss, timeLoss, status);
                risks.addLast(risk);
            }
        } catch (SQLException ex) {
        }
        return risks;
    }
}