import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StaticContentHandler implements HttpHandler {

    private static final String error404Message = "<h1>File Not Found</h1>";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("encoding", "UTF-8");

        try {

            System.out.print(exchange.getRemoteAddress() + " requested resource at " + exchange.getRequestURI());

            String outMsg = getStaticFileContent(exchange);

            System.out.println(" - Found!\n");

            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().write(outMsg.getBytes(StandardCharsets.UTF_8));

            exchange.close();

        } catch (FileNotFoundException e) {
            System.out.println(" - File Not Found.\n");
            exchange.sendResponseHeaders(404, error404Message.length());
            exchange.getResponseBody().write(error404Message.getBytes(StandardCharsets.UTF_8));
            exchange.close();
        }
    }

    // Returns content of file described in HTTP request
    private static String getStaticFileContent(HttpExchange exchange) throws IOException {

        String fileName = tpbsca.STATIC_ROOT + exchange.getRequestURI();

        FileReader fr = new FileReader(fileName);
        BufferedReader bread = new BufferedReader(fr);

        String line;
        // Loads file into string
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bread.readLine()) != null) {
            stringBuilder.append(line);
        }

        bread.close();
        return stringBuilder.toString();
    }
}
