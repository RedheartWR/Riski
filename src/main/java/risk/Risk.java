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
            this.assigneeEmail = result.getString("assigneeEmail");
            this.creatorEmail = result.getString("creatorEmail");
            this.creationDate = result.getDate("creationDate");
            this.lastUpdateDate = result.getDate("lastUpdateDate");
            this.possibility = result.getDouble("possibility");
            this.moneyLoss = result.getDouble("moneyLoss");
            this.timeLoss = result.getDouble("timeLoss");
            this.status = result.getString("status");
        } catch (SQLException ex) {
            // TODO: handle
        }
    }

    public static LinkedList<Risk> fromResultSet(ResultSet result) {
        LinkedList<Risk> risks = new LinkedList<>();
        try {
            while (result.next()) {
                Risk risk = new Risk(result);
                risks.addLast(risk);
            }
        } catch (SQLException ex) {
            // TODO: handle
        }
        return risks;
    }
}