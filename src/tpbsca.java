import com.sun.net.httpserver.HttpServer;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class tpbsca {

    // parameters
    static final int port = 7654;     // Default = 7654
    static final int poolSize = 2;    // Default = 2
    public static final Hashtable<String, HttpServlet> servletHashtable = new Hashtable<>();

    public static final String STATIC_ROOT = System.getProperty("user.dir") + File.separator + "staticcontentrepository";
    public static final String DYNAMIC_ROOT = System.getProperty("user.dir") + File.separator + "servletrepository";

    // Receiving an HTTP request that matches this will shut the server down.
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    public static void main(String[] args) {

        HttpServer server = null;

        // Executes received requests using a thread pool (up to poolSize tasks concurrently).
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        try {
            server = HttpServer.create(new InetSocketAddress(port), poolSize);

            // Static pages are served static content...
            server.createContext("/", new StaticContentHandler());

            // ...And dynamic pages will invoke their servlet.
            server.createContext("/servlet/", new ServletRequestHandler());

            // All threads will be run by the Executor Service using our threadpool
            server.setExecutor(pool);

            // Another context reserved to the input of the shutdown command.
            server.createContext(SHUTDOWN_COMMAND, new ShutdownHandler(pool));

            // The HTTP server is started in a background thread.
            // It will listen to incoming connections, assign each request a task (a RequestHandler object)
            // and give it to the Executor Service, which will manage it according to its policy.
            server.start();
            System.out.println("Thread Pool-Based Servlet Container (with Annotation support)" +
                    " started successfully and listening on port " + port + "!");

            System.out.println("Static webserver root: " + STATIC_ROOT);
            System.out.println("Dynamic webserver root: " + DYNAMIC_ROOT);

            // Also, start the management console

            ManagementConsole console = new ManagementConsole(pool);
            console.start();


        } catch (IOException e) {
            System.out.println("IOException while configuring server. Program will now exit.");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.exit(-1);
        }

        //noinspection StatementWithEmptyBody
        while (!pool.isShutdown()) {
            // Idling until the pool is shutdown from either console or a /SHUTDOWN request.
        }

        // After shutdown, all existing tasks have 1 second to finish.
        server.stop(1);
        System.out.println("Server shut down.");
    }
}
