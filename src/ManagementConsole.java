import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class ManagementConsole extends Thread {

    ExecutorService services;
    ManagementConsole(ExecutorService pool) {
        services = pool;
    }

    void listServlets() {

        // TODO listare quelli nella repo, non quelli caricati.

        int nloaded = tpbsc.servletHashtable.size();
        if(nloaded == 0) {
            System.out.println("No servlets currently loaded. Try using the \"load <servlet>\" command.");
            return;
        }

        System.out.print("Currently loaded servlets: (" + nloaded + ") - ");
        for(Object key : tpbsc.servletHashtable.keySet()) {
            System.out.print(key + "; ");
        }
        System.out.println();
    }

    void loadServlet(String name) {
        // Check table for servlet

        // If found, print error message, else attempt loading
        if(tpbsc.servletHashtable.containsKey(name)) {
            System.out.println(name + " is already loaded.");
            return;
        }

        // points to ./servletrepository/
        String repopath = tpbsc.DYNAMIC_ROOT + File.separator;

        File servletDir = new File(repopath + name);
        if(!servletDir.exists() || !servletDir.isDirectory()) {
            System.out.println(name + " is not a Servlet Directory.");
            return;
        }

        try(FileReader fr = new FileReader(servletDir + File.separator + "metadata.txt")) {

            BufferedReader br = new BufferedReader(fr);

            String line;
            while (( line = br.readLine()) != null ) {

                if (line.contains("ServletClassName")) {
                    String[] words = line.split("=");
                    String servletName = words[1];

                    URL[] url = {new File(servletDir + File.separator + "class" + File.separator).toURI().toURL()};

                    URLClassLoader loader = new URLClassLoader(url);
                    Class<?> servletClass = loader.loadClass(servletName);

                    tpbsc.servletHashtable.put(servletName,
                            (HttpServlet) servletClass.getDeclaredConstructor().newInstance());

                    loader.close();
                    System.out.println(servletName + " loaded successfully.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading servlet " + name + " : " + e.getMessage());
            e.printStackTrace();
        }

    }

    void unloadServlet(String name) {

        if(!tpbsc.servletHashtable.containsKey(name)) {
            System.out.println(name + " is not loaded.");
            return;
        }

        tpbsc.servletHashtable.remove(name);
        System.out.println(name + " unloaded successfully.");
    }


    public void execute(String cmd) {

        cmd = cmd.trim();
        String[] words = cmd.split(" ");

        switch (words[0]) {
            case "load" : {
                loadServlet(words[1]);
                return;
            }
            case "unload" : {
                unloadServlet(words[1]);
                return;
            }

            case "list" : {
                listServlets();
                return;
            }

            case "quit" : return;

            default: System.out.println("Unknown command: " + cmd + "\nSupported commands are: load <servlet>, unload <servlet>, list, quit");
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

        } while (command != null && !command.equals("quit"));

        services.shutdown();
    }

}
