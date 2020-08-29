package risk;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.ConverterJSON;
import tools.HttpMethods;
import user.UserQueries;

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
        String token = headers.getFirst("X-Token");
        String response;

        try {
            if (!UserQueries.checkAuthorization(token))
                throw new Exception("Unauthorized");

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

    private String riskHandler(String method, Headers headers) throws Exception {
        RiskLocal riskLocal = new RiskLocal(headers);

        switch (method) {
            case HttpMethods.GET:
                Risk risk = RiskQueries.getRiskById(riskLocal.id);
                return ConverterJSON.toJSON(risk);
            case HttpMethods.POST:
                Risk oldRisk = RiskQueries.getRiskById(riskLocal.id);
                riskLocal.findAndApplyDiff(oldRisk);
                return RiskQueries.updateRisk(riskLocal.id, riskLocal.name, riskLocal.description, riskLocal.assigneeEmail, riskLocal.creatorEmail,
                        riskLocal.creationDate, riskLocal.lastUpdateDate, riskLocal.possibility, riskLocal.moneyLoss, riskLocal.timeLoss, riskLocal.status);
            case HttpMethods.PUT:
                return RiskQueries.createRisk(riskLocal.id, riskLocal.name, riskLocal.description, riskLocal.assigneeEmail, riskLocal.creatorEmail,
                        riskLocal.creationDate, riskLocal.lastUpdateDate, riskLocal.possibility, riskLocal.moneyLoss, riskLocal.timeLoss, riskLocal.status);
            case HttpMethods.DELETE:
                riskLocal.id = Integer.parseInt(headers.getFirst("Id"));
                return RiskQueries.deleteRisk(riskLocal.id);
        }
        return "";
    }

    private class RiskLocal {
        DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        private static final double NotExisting = Double.NEGATIVE_INFINITY;

        int id;
        double timeLoss;
        double possibility;
        double moneyLoss;
        Date lastUpdateDate;
        Date creationDate;
        String status;
        String name;
        String description;
        String creatorEmail;
        String assigneeEmail;

        RiskLocal(Headers headers) throws ParseException {
            this.id = Integer.parseInt(headers.getFirst("Id"));
            this.name = headers.getFirst("Name");
            this.description = headers.getFirst("Description");
            this.assigneeEmail = headers.getFirst("AssigneeEmail");
            this.creatorEmail = headers.getFirst("CreatorEmail");
            this.creationDate = headers.getFirst("CreationDate").isEmpty() ? null : format.parse(headers.getFirst("CreationDate"));
            this.lastUpdateDate = headers.getFirst("LastUpdateDate").isEmpty() ? null : format.parse(headers.getFirst("LastUpdateDate"));
            this.possibility = headers.getFirst("Possibility").isEmpty() ? NotExisting : Double.parseDouble(headers.getFirst("Possibility"));
            this.moneyLoss = headers.getFirst("MoneyLoss").isEmpty() ? NotExisting : Double.parseDouble(headers.getFirst("MoneyLoss"));
            this.timeLoss = headers.getFirst("timeLoss").isEmpty() ? NotExisting : Double.parseDouble(headers.getFirst("timeLoss")); //TODO: timeLoss in small case
            this.status = headers.getFirst("Status");
        }

        public void findAndApplyDiff(Risk oldRisk) {
            if (id == -1)
                id = oldRisk.id;
            if (timeLoss == NotExisting)
                timeLoss = oldRisk.timeLoss;
            if (possibility == NotExisting)
                possibility = oldRisk.possibility;
            if (moneyLoss == NotExisting)
                moneyLoss = oldRisk.moneyLoss;
            if (lastUpdateDate == null)
                lastUpdateDate = oldRisk.lastUpdateDate;
            if (creationDate == null)
                creationDate = oldRisk.creationDate;
            if (status == null || status.isEmpty())
                status = oldRisk.status;
            if (name == null || name.isEmpty())
                name = oldRisk.name;
            if (description == null || description.isEmpty())
                description = oldRisk.description;
            if (creatorEmail == null || creatorEmail.isEmpty())
                creatorEmail = oldRisk.creatorEmail;
            if (assigneeEmail == null || assigneeEmail.isEmpty())
                assigneeEmail = oldRisk.assigneeEmail;
        }
    }
}

