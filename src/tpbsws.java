import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

// Thread Pool Based Static WebServer
public class tpbsws {

    // parameters
    static int port = 7654;
    static int poolSize = 2;

    public static final String WEB_ROOT = "C:/Temp/WebContent";
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {

        HttpServer server;

        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        try {
            server = HttpServer.create(new InetSocketAddress(port), 5);

            HttpContext context = server.createContext("/");
            RequestHandler handler = new RequestHandler();
            context.setHandler(handler);

            server.setExecutor(pool);
            server.start();

            server.

        } catch (IOException e) {
            System.out.println("IOException while configuring server. Program will now exit.");
            e.printStackTrace();
            System.exit(-1);
        }

        while (true) {
            // Accetta connessione
            try {

                // Assegnagli una task
                Runnable r;
                // Invia la task alla thread pool
                pool.execute(r);

            } catch (IOException e) {
                System.out.println("IOException while accepting connection. Program will now exit.");
                e.printStackTrace();
                System.exit(-1);
            }


        }
    }
}