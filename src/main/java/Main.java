import com.sun.net.httpserver.HttpServer;
import user.UserHandler;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(UserHandler.USER, new UserHandler());
        server.createContext(UserHandler.USERS, new UserHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}