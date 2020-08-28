package risk;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RiskHandler implements HttpHandler {

    public static final String RISKSBYGROUP = "/risks/bygroup";
    public static final String RISKSBYUSER = "/risks/byuser";
    public static final String RISKSBYID = "/risk";
    public static final String RISKCREATE = "/risk/create";
    public static final String RISKDELETE = "/risk/delete";


    public void handle(HttpExchange t) throws IOException {
        Headers tmp = t.getRequestHeaders();
        String response;
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
        try {
            switch (t.getRequestURI().toString()) {
                case RISKSBYGROUP:
                    groupId = Integer.parseInt(tmp.getFirst("GroupId"));
                    response = RiskQueries.getrisksByGroup(groupId);
                    break;
                case RISKSBYID:
                    id = Integer.parseInt(tmp.getFirst("Id"));
                    response = RiskQueries.getriskById(id);
                    break;
                case RISKSBYUSER:
                    assigneeEmail = tmp.getFirst("X-Email");
                    response = RiskQueries.getriskByUser(assigneeEmail);
                    break;
                case RISKCREATE:
                    id = Integer.parseInt(tmp.getFirst("Id"));
                    name = tmp.getFirst("Name");
                    description = tmp.getFirst("Description");
                    assigneeEmail = tmp.getFirst("assigneeEmail");
                    creatorEmail = tmp.getFirst("creatorEmail");
                    DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                    creationDate = format.parse(tmp.getFirst("CreationDate"));
                    lastUpdateDate = format.parse(tmp.getFirst("LastUpdateDate"));
                    possibility = Double.parseDouble(tmp.getFirst("Possibility"));
                    moneyLoss = Double.parseDouble(tmp.getFirst("MoneyLoss"));
                    timeLoss = Double.parseDouble(tmp.getFirst("timeLoss"));
                    groupId = Integer.parseInt(tmp.getFirst("GroupId"));
                    status = tmp.getFirst("Status");
                    response = RiskQueries.createRisk(id, name, description, assigneeEmail, creatorEmail,
                            creationDate, lastUpdateDate, possibility, moneyLoss, timeLoss, groupId, status);
                    break;
                case RISKDELETE:
                    id = Integer.parseInt(tmp.getFirst("Id"));
                    response = RiskQueries.deleteRisk(id);
                    break;
                default:
                    throw new NoSuchMethodException();
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }

    }

}

