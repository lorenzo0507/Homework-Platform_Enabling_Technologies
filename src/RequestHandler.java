import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements HttpHandler {

    private static final String header =
            """
                    HTTP/1.1 200 OK\r
                    Content-Type: text/html\r
                    Content-Length:\s""";

    private static final String postfix = "\r\n \r\n";

    private static final String error404Message =
            """
                    HTTP/1.1 404 File Not Found\r
                    Content-Type: text/html\r
                    Content-Length: 23\r
                    \r
                    <h1>File Not Found</h1>""";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("encoding", "UTF-8");

        try {

            String fileName = tpbsws.WEB_ROOT + exchange.getRequestURI();
            FileReader fr = new FileReader(fileName);
            BufferedReader bread = new BufferedReader(fr);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bread.readLine()) != null) {
                stringBuilder.append(line);
            }
            bread.close();
            String outMsg = header + stringBuilder.length() + postfix + stringBuilder;

            exchange.sendResponseHeaders(200, outMsg.length());
            exchange.getResponseBody().write(outMsg.getBytes(StandardCharsets.UTF_8));
            exchange.close();

        } catch (FileNotFoundException e) {
            exchange.getResponseBody().write(error404Message.getBytes());
        }
        }
    }
