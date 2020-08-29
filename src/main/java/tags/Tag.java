package tags;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Tag implements Serializable {
    String riskId;
    String tagName;

    public static LinkedList<String> tagsFromResultSet(ResultSet result) {
        LinkedList<String> tags = new LinkedList<>();
        try {
            while (result.next()) {
                String name = result.getString("tag_name");
                tags.addLast(name);
            }
        } catch (SQLException ex) {
        }
        return tags;
    }

    public static LinkedList<String> risksFromResultSet(ResultSet result) {
        LinkedList<String> risks = new LinkedList<>();
        try {
            while (result.next()) {
                String riskId = result.getString("risk_id");
                risks.addLast(riskId);
            }
        } catch (SQLException ex) {
        }
        return risks;
    }
}
