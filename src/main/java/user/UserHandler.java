package user;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class UserHandler implements HttpHandler {

    public static final String USER = "/user";
    public static final String USERS = "/users";
    public static final String USERCREATE = "/user/create";
    public static final String USERDELETE = "/user/delete";
    public static final String AUTHORIZATION = "/authorization";


    public void handle(HttpExchange t) throws IOException {
        Headers tmp = t.getRequestHeaders();
        String response;
        String userEmail;
        String userPassword;
        String userName;
        String isAHead;
        try {
        switch (t.getRequestURI().toString()) {
            case USER:
                userEmail = tmp.getFirst("X-Email");
                response = UserQueries.getUser(userEmail);
                break;
            case USERS:
                response = UserQueries.getUsers();
                break;
            case USERCREATE:
                userEmail = tmp.getFirst("X-Email");
                userName = tmp.getFirst("Name");
                userPassword = tmp.getFirst("Password");
                isAHead = tmp.getFirst("IsAHead").equals("Y") ? "TRUE" : "FALSE";
                response = UserQueries.createUser(userEmail, userName, userPassword, isAHead);
                break;
            case USERDELETE:
                userEmail = tmp.getFirst("X-Email");
                response = UserQueries.deleteUser(userEmail);
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
        catch (UnsupportedOperationException ex) {
            t.sendResponseHeaders(500, 0);
        }
        catch (NoSuchMethodException ex) {
            t.sendResponseHeaders(400, 0);
        }

    }

}
