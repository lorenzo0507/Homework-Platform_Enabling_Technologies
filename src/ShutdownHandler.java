import com.sun.net.httpserver.*;
import java.util.concurrent.ExecutorService;

// Special case for when SHUTDOWN_COMMAND is received.
// Shuts down pool, which then kills HttpServer in main thread and exits program
public class ShutdownHandler implements HttpHandler {

    private final ExecutorService t;
    ShutdownHandler(ExecutorService pool) { t = pool; }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println(exchange.getRemoteAddress() + " requested server shutdown.");
        System.out.println("Shutting down...");
        t.shutdown();
    }
}