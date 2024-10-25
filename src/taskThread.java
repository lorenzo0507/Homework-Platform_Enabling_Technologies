import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.Socket;

public class taskThread implements Runnable {

    private Socket s;

    public taskThread(Socket socket) {
        //TODO constructor
        this.s = socket;
    }

    @Override
    public void run() {
        //TODO
    }
}

