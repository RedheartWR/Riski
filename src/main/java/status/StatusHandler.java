package status;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import user.UserQueries;

import java.io.IOException;
import java.io.OutputStream;

public class StatusHandler implements HttpHandler {
    public static final String STATUSES = "/statuses";

    public void handle(HttpExchange t) throws IOException {
        try {
            String response;

            String token = t.getRequestHeaders().getFirst("X-Token");
            if (!UserQueries.isTokenValid(token))
                throw new Exception("Unauthorized");

            if (t.getRequestURI().toString().equals(STATUSES))
                response = StatusQueries.getUsers();
            else
                throw new NoSuchMethodException();

            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }
    }
}
