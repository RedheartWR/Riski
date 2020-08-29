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
        RiskLocal risk = new RiskLocal(headers);

        switch (method) {
            case HttpMethods.GET:
                return RiskQueries.getRiskById(risk.id);
            case HttpMethods.POST:
                return "";
            case HttpMethods.PUT:
                risk = new RiskLocal(headers);
                return RiskQueries.createRisk(risk.id, risk.name, risk.description, risk.assigneeEmail, risk.creatorEmail,
                        risk.creationDate, risk.lastUpdateDate, risk.possibility, risk.moneyLoss, risk.timeLoss, risk.status);
            case HttpMethods.DELETE:
                risk.id = Integer.parseInt(headers.getFirst("Id"));
                return RiskQueries.deleteRisk(risk.id);
        }
        return "";
    }

    private class RiskLocal {
        DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

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

        RiskLocal(Headers headers) throws ParseException {
            this.id = Integer.parseInt(headers.getFirst("Id"));
            this.name = headers.getFirst("Name");
            this.description = headers.getFirst("Description");
            this.assigneeEmail = headers.getFirst("AssigneeEmail");
            this.creatorEmail = headers.getFirst("CreatorEmail");
            this.creationDate = format.parse(headers.getFirst("CreationDate"));
            this.lastUpdateDate = format.parse(headers.getFirst("LastUpdateDate"));
            this.possibility = Double.parseDouble(headers.getFirst("Possibility"));
            this.moneyLoss = Double.parseDouble(headers.getFirst("MoneyLoss"));
            this.timeLoss = Double.parseDouble(headers.getFirst("timeLoss"));
            this.status = headers.getFirst("Status");
        }
    }
}

