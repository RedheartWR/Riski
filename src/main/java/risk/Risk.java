package risk;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Risk {
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
    Integer groupId;
    String status;
}
