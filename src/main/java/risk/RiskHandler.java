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

        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, " +
                "X-Id, " +
                "X-Token, " +
                "X-Name, " +
                "X-Description," +
                "X-AssigneeEmail, " +
                "X-CreatorEmail, " +
                "X-CreationDate, " +
                "X-LastUpdateDate, " +
                "X-Possibility, " +
                "X-MoneyLoss, " +
                "X-TimeLoss, " +
                "X-Status");
        t.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");

        if (method.equals("OPTIONS")){
            t.sendResponseHeaders(200, 0);
            return;
        }

        try {
            if (!UserQueries.isTokenValid(token))
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

            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }

    }

    private String risksHandler() throws Exception {
        return RiskQueries.getRisks();
    }

    private String risksByUserHandler(Headers headers) throws Exception {
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
                riskLocal.id = Integer.parseInt(headers.getFirst("X-Id"));
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
            this.id = !headers.containsKey("X-Id") ? -1 : Integer.parseInt(headers.getFirst("X-Id"));
            this.name = !headers.containsKey("X-Name") ? null : headers.getFirst("X-Name");
            this.description = !headers.containsKey("X-Description") ? null : headers.getFirst("X-Description");
            this.assigneeEmail = !headers.containsKey("X-AssigneeEmail") ? null : headers.getFirst("X-AssigneeEmail");
            this.creatorEmail = !headers.containsKey("X-CreatorEmail") ? null : headers.getFirst("X-CreatorEmail");
            this.creationDate = !headers.containsKey("X-CreationDate") ? new Date(1) : format.parse(headers.getFirst("X-CreationDate"));
            this.lastUpdateDate = !headers.containsKey("X-LastUpdateDate") ? new Date(1) : format.parse(headers.getFirst("X-LastUpdateDate"));
            this.possibility = !headers.containsKey("X-Possibility") ? NotExisting : Double.parseDouble(headers.getFirst("X-Possibility"));
            this.moneyLoss = !headers.containsKey("X-MoneyLoss") ? NotExisting : Double.parseDouble(headers.getFirst("X-MoneyLoss"));
            this.timeLoss = !headers.containsKey("X-TimeLoss") ? NotExisting : Double.parseDouble(headers.getFirst("X-TimeLoss")); //TODO: timeLoss in small case
            this.status = !headers.containsKey("X-Status") ? null : headers.getFirst("X-Status");
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
            if (lastUpdateDate.equals(new Date(1)))
                lastUpdateDate = oldRisk.lastUpdateDate;
            if (creationDate.equals(new Date(1)))
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

