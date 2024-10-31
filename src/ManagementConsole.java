import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ManagementConsole extends Thread {

    String repopath;
    ExecutorService services;
    ManagementConsole(ExecutorService pool) {
        services = pool;
        repopath = tpbsc.DYNAMIC_ROOT + File.separator;
    }

    void listAvailableServlets() {

        // TODO listare quelli nella repo, non quelli caricati.
        File dir = new File(repopath);
        File[] files = dir.listFiles();

        if (files == null) {
            System.out.println("Found no servlets in repository.");
            return;
        }

        List<String> servlets = new java.util.ArrayList<>();
        for (File folder : files) {
            File data = new File(folder + File.separator + "metadata.txt");

            try(FileReader fr = new FileReader(data)) {
                BufferedReader br = new BufferedReader(fr);

                String line;
                while (( line = br.readLine()) != null ) {

                    if (line.contains("ServletClassName")) {
                        String[] words = line.split("=");
                        servlets.add(words[1]);
                        break;
                    }
                }
            } catch (Exception _) {}
        }

        System.out.print("Found (" + servlets.size() + ") servlets in repository - ");
        for (String servlet : servlets) {
            System.out.print(servlet + "; ");
        }
        System.out.println();

    }

    void listLoadedServlets() {

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

        if (tpbsc.servletHashtable.containsKey(name)) {
            System.out.println(name + " is already loaded.");
            return;
        }

        // points to ./servletrepository/

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
                listAvailableServlets();
                return;
            }

            case "listloaded" : {
                listLoadedServlets();
                return;
            }

            case "quit" : return;

            default:
                System.out.println("Unknown command: " + cmd + "\nSupported commands are: load <servlet>, unload <servlet>, list, listloaded, quit");
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

        } while (command != null && !command.equals("quit") && !services.isShutdown());

        services.shutdown();
    }

}
