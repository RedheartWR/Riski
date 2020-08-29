package user;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;

import java.io.IOException;
import java.io.OutputStream;

public class UserHandler implements HttpHandler {

    public static final String USER = "/user";
    public static final String USERS = "/users";
    public static final String AUTHORIZATION = "/authorization";

    public void handle(HttpExchange t) throws IOException {
        Headers headers = t.getRequestHeaders();
        String response = "";
        String userEmail = headers.getFirst("X-Email");
        String userPassword = headers.getFirst("Password");
        String newPassword = headers.getFirst("NewPassword");
        String userName = headers.getFirst("Name");
        String isAHead = headers.getFirst("IsAHead").equals("Y") ? "TRUE" : "FALSE";

        String token = headers.getFirst("X-Token");
        String method = t.getRequestMethod();

        try {
            if (!t.getRequestURI().toString().equals(AUTHORIZATION) && !UserQueries.isTokenValid(token))
                throw new Exception("Unauthorized");

            switch (t.getRequestURI().toString()) {
                case USER:
                    switch (method) {
                        case HttpMethods.GET:
                            response = UserQueries.getUser(userEmail);
                            break;
                        case HttpMethods.POST:
                            response = UserQueries.changePassword(userEmail, userPassword, newPassword);
                            break;
                        case HttpMethods.PUT:
                            response = UserQueries.createUser(userEmail, userName, userPassword, isAHead);
                            break;
                        case HttpMethods.DELETE:
                            response = UserQueries.deleteUser(userEmail);
                            break;
                    }
                    break;
                case USERS:
                    response = UserQueries.getUsers();
                    break;
                case AUTHORIZATION:
                    response = UserQueries.authorization(userEmail, userPassword);
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
}
