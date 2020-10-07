package tags;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;
import user.UserQueries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

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

        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-TagName, X-Token");
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
                case TAG:
                    switch (method) {
                        case HttpMethods.PUT:
                            response = TagQueries.createTag(tagName, riskId);
                            break;
                        case HttpMethods.POST:
                            changeTags(t, riskId);
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

            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }
    }

    private void changeTags(HttpExchange t, String riskId) throws Exception {
        InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        List<String> tags = new Gson().fromJson(buf.toString(),
                new TypeToken<List<String>>() {}.getType());

        TagQueries.deleteTags(riskId);

        for (String tag : tags) {
            TagQueries.createTag(tag, riskId);
        }

        br.close();
        isr.close();
    }
}
