import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class ManagementConsole extends Thread {

    void loadServlet(String name) {
        // Check table for servlet

        // If found, print error message, else attempt loading
        if(tpbsc.servletHashtable.containsKey(name)) {
            System.out.println(name + " is already loaded.");
            return;
        }

        File dir = new File(System.getProperty("user.dir") + File.separator + "servletcontainer" + File.separator);

        File[] arr = dir.listFiles();

        // find servlet in repository, check metadata.txt for class name and load it.

    }

    void unloadServlet(String name) {

        if(!tpbsc.servletHashtable.containsKey(name)) {
            System.out.println(name + " is not loaded.");
            return;
        }

        // TODO Actually unload the servlet class

        tpbsc.servletHashtable.remove(name);
    }


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
