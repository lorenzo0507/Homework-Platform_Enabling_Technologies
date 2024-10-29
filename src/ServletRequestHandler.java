import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import javax.servlet.http.HttpServlet;

public class ServletRequestHandler implements HttpHandler {

    ServletRequestHandler() {

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Check if requested servlet is loaded (in the hashtable)
        // If it is, invoke the .service() function of the servlet.
        // Otherwise, return an error to the user



    }

}