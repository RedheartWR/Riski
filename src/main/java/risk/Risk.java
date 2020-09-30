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
            this.id = result.getInt("id");
            this.name = result.getString("name");
            this.description = result.getString("description");
            this.assigneeEmail = result.getString("assignee_email");
            this.creatorEmail = result.getString("creator_email");
            this.creationDate = result.getDate("creation_date");
            this.lastUpdateDate = result.getDate("last_update_date");
            this.possibility = result.getDouble("possibility");
            this.moneyLoss = result.getDouble("money_loss");
            this.timeLoss = result.getDouble("time_loss");
            this.status = result.getString("status");
        } catch (SQLException ex) { }
    }

    public static LinkedList<Risk> fromResultSet(ResultSet result) {
        LinkedList<Risk> risks = new LinkedList<>();
        try {
            while (result.next()) {
                Risk risk = new Risk(result);
                risks.addLast(risk);
            }
        } catch (SQLException ex) {
            return risks;
        }
        return risks;
    }
}