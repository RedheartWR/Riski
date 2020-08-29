package risk;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RiskHandler implements HttpHandler {

    public static final String RISKSBYUSER = "/risks/byuser";
    public static final String RISKS = "/risks";
    public static final String RISK = "/risk";


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
                case RISKSBYUSER:
                    assigneeEmail = tmp.getFirst("X-Email");
                    response = RiskQueries.getRiskByUser(assigneeEmail);
                    break;
                case RISKS:
                    response = RiskQueries.getRisks();
                    break;
                case RISK:
                    String method = t.getRequestMethod();
                    switch (method) {
                        case HttpMethods.GET:
                            id = Integer.parseInt(tmp.getFirst("Id"));
                            response = RiskQueries.getRiskById(id);
                            break;
                        case HttpMethods.POST:
                            break;
                        case HttpMethods.PUT:
                            id = Integer.parseInt(tmp.getFirst("Id"));
                            name = tmp.getFirst("Name");
                            description = tmp.getFirst("Description");
                            assigneeEmail = tmp.getFirst("AssigneeEmail");
                            creatorEmail = tmp.getFirst("CreatorEmail");
                            DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                            creationDate = format.parse(tmp.getFirst("CreationDate"));
                            lastUpdateDate = format.parse(tmp.getFirst("LastUpdateDate"));
                            possibility = Double.parseDouble(tmp.getFirst("Possibility"));
                            moneyLoss = Double.parseDouble(tmp.getFirst("MoneyLoss"));
                            timeLoss = Double.parseDouble(tmp.getFirst("timeLoss"));
                            status = tmp.getFirst("Status");
                            response = RiskQueries.createRisk(id, name, description, assigneeEmail, creatorEmail,
                                    creationDate, lastUpdateDate, possibility, moneyLoss, timeLoss, status);
                            break;
                        case HttpMethods.DELETE:
                            id = Integer.parseInt(tmp.getFirst("Id"));
                            response = RiskQueries.deleteRisk(id);
                            break;
                    }
                default:
                    throw new NoSuchMethodException();
            }
            if (response.startsWith("ERROR"))
                throw new Exception(response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }

    }

}

