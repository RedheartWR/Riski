package tags;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;
import user.UserQueries;

import java.io.IOException;
import java.io.OutputStream;

public class TagHandler implements HttpHandler {

    public static final String TAG = "/tag";
    public static final String TAGS = "/tags";
    public static final String RISKSFORTAG = "/tag/risks";

    public void handle(HttpExchange t) throws IOException {
        Headers headers = t.getRequestHeaders();
        String response = "";
        String tagName = headers.getFirst("X-TagName");
        String riskId = headers.getFirst("X-RiskId");

        String token = headers.getFirst("X-Token");
        String method = t.getRequestMethod();

        try {
            if (!UserQueries.isTokenValid(token))
                throw new Exception("Unauthorized");

            switch (t.getRequestURI().toString()) {
                case TAG:
                    switch (method) {
                        case HttpMethods.PUT:
                            response = TagQueries.createTag(tagName, riskId);
                            break;
                        case HttpMethods.DELETE:
                            response = TagQueries.deleteTag(tagName, riskId);
                            break;
                    }
                    break;
                case TAGS:
                    response = TagQueries.getTags(riskId);
                    break;
                case RISKSFORTAG:
                    response = TagQueries.getRisks(tagName);
                    break;
                default:
                    throw new NoSuchMethodException();
            }

            if (response.contains("Error"))
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
