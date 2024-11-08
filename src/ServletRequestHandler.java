import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import javax.servlet.http.HttpServlet;

public class ServletRequestHandler implements HttpHandler {

    ServletRequestHandler() {



    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Check if requested servlet is loaded.
        // If it is, invoke the .service() function of the servlet.
        // Otherwise, return an error to the user

        String[] splitUri = exchange.getRequestURI().toString().split("/");
        String servletName = splitUri[splitUri.length - 1];

        if (tpbsca.servletHashtable.containsKey(servletName)) {
            try {
                HttpServlet servlet = tpbsca.servletHashtable.get(servletName);

                MyRequestImpl request = new MyRequestImpl(exchange);
                MyResponseImpl response = new MyResponseImpl(exchange);

                servlet.service(request, response);
                exchange.close();

            }
            catch (Exception e) {
                System.out.println("There was an error while trying to handle request.");

                //noinspection CallToPrintStackTrace
                e.printStackTrace();

            }
        }
        else {
            exchange.sendResponseHeaders(404, "Servlet not loaded.".length());
            exchange.getResponseBody().write("Servlet not loaded.".getBytes());
        }
    }


}