package risk;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RiskHandler implements HttpHandler {

    public static final String RISKSBYUSER = "/risks/byuser";
    public static final String RISKS = "/risks";
    public static final String RISK = "/risk";

    public void handle(HttpExchange t) throws IOException {
        Headers headers = t.getRequestHeaders();
        String method = t.getRequestMethod();
        String response;
        try {
            switch (t.getRequestURI().toString()) {
                case RISKSBYUSER:
                    response = risksByUserHandler(headers);
                    break;
                case RISKS:
                    response = risksHandler();
                    break;
                case RISK:
                    response = riskHandler(method, headers);
                    break;
                default:
                    throw new NoSuchMethodException();
            }

            if (response.contains("ERROR"))
                throw new Exception(response);

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }

    }

    private String risksHandler() {
        return RiskQueries.getRisks();
    }

    private String risksByUserHandler(Headers headers) {
        String assigneeEmail = headers.getFirst("X-Email");
        return RiskQueries.getRiskByUser(assigneeEmail);
    }

    private String riskHandler(String method, Headers headers) throws ParseException {
        double timeLoss;
        String status;
        String name;
        Date lastUpdateDate;
        Date creationDate;
        int id;
        double moneyLoss;
        String description;
        String creatorEmail;
        double possibility;
        String assigneeEmail;

        switch (method) {
            case HttpMethods.GET:
                id = Integer.parseInt(headers.getFirst("Id"));
                return RiskQueries.getRiskById(id);
            case HttpMethods.POST:
                return "";
            case HttpMethods.PUT:
                id = Integer.parseInt(headers.getFirst("Id"));
                name = headers.getFirst("Name");
                description = headers.getFirst("Description");
                assigneeEmail = headers.getFirst("AssigneeEmail");
                creatorEmail = headers.getFirst("CreatorEmail");
                DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                creationDate = format.parse(headers.getFirst("CreationDate"));
                lastUpdateDate = format.parse(headers.getFirst("LastUpdateDate"));
                possibility = Double.parseDouble(headers.getFirst("Possibility"));
                moneyLoss = Double.parseDouble(headers.getFirst("MoneyLoss"));
                timeLoss = Double.parseDouble(headers.getFirst("timeLoss"));
                status = headers.getFirst("Status");
                return RiskQueries.createRisk(id, name, description, assigneeEmail, creatorEmail,
                        creationDate, lastUpdateDate, possibility, moneyLoss, timeLoss, status);
            case HttpMethods.DELETE:
                id = Integer.parseInt(headers.getFirst("Id"));
                return RiskQueries.deleteRisk(id);
        }
        return "";
    }
}

