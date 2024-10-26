import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Thread Pool Based Static WebServer
public class tpbsws {

    // parameters
    static int port = 7654;     // Default = 7654
    static int poolSize = 2;    // Default = 2

    // CHANGE THIS TO THE FOLDER WHERE WEB DATA IS STORED
    public static final String WEB_ROOT = System.getProperty("user.dir") + "/staticcontentrepository";

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    public static void main(String[] args) {

        HttpServer server = null;

        // Executes received requests using a thread pool (up to poolSize tasks concurrently).
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        try {
            server = HttpServer.create(new InetSocketAddress(port), poolSize);

            // A context is created that will assign every matching URI request to a RequestHandler() task...
            server.createContext("/", new RequestHandler());

            //...which will be run by the Executor Service according to its policy (a threadPool in our case)
            server.setExecutor(pool);

            // Another context reserved to the input of the shutdown command. This just sets
            server.createContext(SHUTDOWN_COMMAND, new ShutdownHandler(pool));

            // The HTTP server is started in a background thread.
            // It will listen to incoming connections, assign each request a task (a RequestHandler object)
            // and give it to the Executor Service, which will manage it according to its policy.
            server.start();
            System.out.println("Thread Pool-Based Static Web Server started successfully and listening on port " + port + "!");

        } catch (IOException e) {
            System.out.println("IOException while configuring server. Program will now exit.");
            e.printStackTrace();
            System.exit(-1);
        }

        while (!pool.isShutdown()) {
            // Idling until (the pool's) shutdown
        }

        // After shutdown, all existing tasks have 1 second to finish.
        server.stop(1);
        System.out.println("Server shut down.");
    }
}

// Special case for when SHUTDOWN_COMMAND is received.
// Shuts down pool, which then kills HttpServer in main thread and exits program
class ShutdownHandler implements HttpHandler {

    private final ExecutorService t;
    ShutdownHandler(ExecutorService pool) { t = pool; }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println(exchange.getRemoteAddress() + " requested server shutdown.");
        System.out.println("Shutting down...");
        t.shutdown();
    }
}