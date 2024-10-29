import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class ManagementConsole extends Thread {

    public void execute(String cmd) {

        cmd = cmd.trim();
        String[] words = cmd.split(" ");

        switch (words[0]) {
            case "load" : // loadServlet(words[1]);
            case "unload" : // unloadServlet(words[1]);
            case "quit" : return;

            default: System.out.println("Unknown command: " + cmd + "\nSupported commands are: load <servlet>, unload <servlet>, quit");
        }

    }

    public void run() {

        String command = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.print("Command: ");

            try {
                command = br.readLine();
                execute(command);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (!command.equals("quit"));

    }

}
