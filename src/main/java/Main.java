import com.sun.net.httpserver.HttpServer;
import risk.RiskHandler;
import status.StatusHandler;
import tags.TagHandler;
import user.UserHandler;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext(UserHandler.USER, new UserHandler());
        server.createContext(UserHandler.USERS, new UserHandler());
        server.createContext(UserHandler.AUTHORIZATION, new UserHandler());

        server.createContext(RiskHandler.RISK, new RiskHandler());
        server.createContext(RiskHandler.RISKS, new RiskHandler());
        server.createContext(RiskHandler.RISKSBYUSER, new RiskHandler());

        server.createContext(StatusHandler.STATUSES, new StatusHandler());

        server.createContext(TagHandler.TAG, new TagHandler());
        server.createContext(TagHandler.TAGS, new TagHandler());
        server.createContext(TagHandler.RISKSFORTAG, new TagHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}