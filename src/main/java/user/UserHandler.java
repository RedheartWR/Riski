package user;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.HttpMethods;

import java.io.IOException;
import java.io.OutputStream;

public class UserHandler implements HttpHandler {

    public static final String USER = "/user"; // -> GET "/user"
    public static final String USERS = "/users";
    //СИСТЕМА ТОКЕНОВ
    public static final String AUTHORIZATION = "/authorization";


/*
    https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
    CODE:
    200 OK
    400 Bad request
    401 Unauthorized
    404 Not found
    500 Server error
*/

    public void handle(HttpExchange t) throws IOException {
        Headers tmp = t.getRequestHeaders();
        String response = null;
        String userEmail;
        String userPassword;
        String userName;
        String isAHead;
        try {
        switch (t.getRequestURI().toString()) {
            case USER:
                String method = t.getRequestMethod();
                switch (method) {
                    case HttpMethods.GET:
                        userEmail = tmp.getFirst("X-Email");
                        response = UserQueries.getUser(userEmail);
                        break;
                    case HttpMethods.POST:
                        break;
                    case HttpMethods.PUT:
                        userEmail = tmp.getFirst("X-Email");
                        userName = tmp.getFirst("Name");
                        userPassword = tmp.getFirst("Password");
                        isAHead = tmp.getFirst("IsAHead").equals("Y") ? "TRUE" : "FALSE";
                        response = UserQueries.createUser(userEmail, userName, userPassword, isAHead);
                        break;
                    case HttpMethods.DELETE:
                        userEmail = tmp.getFirst("X-Email");
                        response = UserQueries.deleteUser(userEmail);
                        break;
                }
                break;
            case USERS:
                response = UserQueries.getUsers();
                break;
            case AUTHORIZATION:
                userEmail = tmp.getFirst("X-Email");
                userPassword = tmp.getFirst("Password");
                response = UserQueries.authorization(userEmail, userPassword);
                break;
            default:
                throw new NoSuchMethodException();
        }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        catch (Exception ex) {
            t.sendResponseHeaders(500, 0);
        }

    }

}
