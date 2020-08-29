package tags;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;

import java.io.IOException;
import java.io.OutputStream;

public class TagHandler implements HttpHandler {

    public static final String TAG = "/tag"; // -> GET "/user"
    public static final String TAGS = "/tags";
    public static final String RISKSFORTAG = "/tag/risks";

    public void handle(HttpExchange t) throws IOException {
        Headers tmp = t.getRequestHeaders();
        String response = null;
        String tagName;
        String riskId;
        try {
            switch (t.getRequestURI().toString()) {
                case TAG:
                    String method = t.getRequestMethod();
                    switch (method) {
                        case HttpMethods.PUT:
                            tagName = tmp.getFirst("TagName");
                            riskId = tmp.getFirst("riskId");
                            response = TagQueries.createTag(tagName, riskId);
                            break;
                        case HttpMethods.DELETE:
                            tagName = tmp.getFirst("TagName");
                            riskId = tmp.getFirst("riskId");
                            response = TagQueries.deleteTag(tagName, riskId);
                            break;
                    }
                    break;
                case TAGS:
                    riskId = tmp.getFirst("riskId");
                    response = TagQueries.getTags(riskId);
                    break;
                case RISKSFORTAG:
                    tagName = tmp.getFirst("TagName");
                    response = TagQueries.getRisks(tagName);
                    break;
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
