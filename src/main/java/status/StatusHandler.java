package status;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class StatusHandler implements HttpHandler {
    public static final String STATUSES = "/statuses";

    public void handle(HttpExchange t) throws IOException {
        try {
            String response;

            if (t.getRequestURI().toString().equals(STATUSES))
                response = StatusQueries.getUsers();
            else
                throw new NoSuchMethodException();


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
}
