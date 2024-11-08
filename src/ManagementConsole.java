import javax.servlet.http.HttpServlet;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ManagementConsole extends Thread {

    private final String repopath;
    private final ExecutorService services;
    ManagementConsole(ExecutorService pool) {
        services = pool;
        repopath = tpbsca.DYNAMIC_ROOT + File.separator;
    }

    /**
     * Lists all servlets in repository, referred to by their internal name.
     */
    void listAvailableServlets() {

        File dir = new File(repopath);
        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("Found no regular servlets in repository.");
        } else {
            List<String> internalNames = new ArrayList<>();
            for (File folder : files)
                internalNames.add(folder.getName());

            internalNames.remove("class"); // That's the annotated servlets' folder, not a real servlet.
            System.out.print("Found (" + internalNames.size() + ") servlets in repository - ");
            for (String servlet : internalNames)
                System.out.print(servlet + "; ");

            System.out.println();

        }


        List<String> annotatedNames = new ArrayList<>();
        File annotatedRepo = new File(repopath + "class" + File.separator);

        File[] annotatedServlets = annotatedRepo.listFiles();

        if (annotatedServlets == null || annotatedServlets.length == 0) {
            System.out.println("Found no annotated servlets in repository.\n");
        }
        else {
            for (File file : annotatedServlets)
                annotatedNames.add(file.getName().split(".class")[0]);

            System.out.print("Found (" + annotatedNames.size() + ") annotated servlets in repository - ");
            for (String servlet : annotatedNames) {
                System.out.print(servlet + "; ");
            }
            System.out.println();
        }


    }

    /** Lists all currently loaded servlets by their external name, whether they were standard or annotated.
     */
    void listLoadedServlets() {

        int nloaded = tpbsca.servletHashtable.size();
        if(nloaded == 0) {
            System.out.println("No servlets currently loaded. Try using the \"load <servlet>\" command.");
            return;
        }

        System.out.print("Currently loaded servlets: (" + nloaded + ") - ");
        for(Object key : tpbsca.servletHashtable.keySet()) {
            System.out.print(key + "; ");
        }
        System.out.println();

    }

    /** Loads a non-annotated servlet from its respective folder into the container's hash table.
     *
     * @param className name of the file (which must match the class it contains)
     */
    void loadServlet(String className) {
        // Check table for servlet
        // If found, print error message, else attempt loading

        if (tpbsca.servletHashtable.containsKey(className)) {
            System.out.println("A servlet with the name " + className + " is already loaded.");
            return;
        }

        // points to ./servletrepository/

        File servletDir = new File(repopath + className);
        if(!servletDir.isDirectory()) {
            System.out.println(className + " is not a directory in the servlet repository.");
            return;
        }

        try(FileReader fr = new FileReader(servletDir + File.separator + "metadata.txt")) {

            BufferedReader br = new BufferedReader(fr);

            String line;
            while (( line = br.readLine()) != null ) {

                if (line.contains("ServletClassName")) {
                    String[] words = line.split("=");
                    String externalName = words[1];

                    URL[] url = {new File(servletDir + File.separator + "class" + File.separator).toURI().toURL()};

                    URLClassLoader loader = new URLClassLoader(url);
                    Class<?> servletClass = loader.loadClass(className);

                    if(tpbsca.servletHashtable.containsKey(externalName)) {
                        System.out.println("Servlet " + className + " is already loaded as \"" + externalName + "\".");
                        return;
                    }

                    tpbsca.servletHashtable.put(externalName,
                            (HttpServlet) servletClass.getDeclaredConstructor().newInstance());

                    loader.close();
                    System.out.println(className + " successfully loaded as \"" + externalName + "\".");
                    return;
                }
            }
            // Fail case
            System.out.println("Couldn't find valid ServletClassName property for " + className +".\n" +
                    "Check the corresponding metadata.exe file.");


        } catch (Exception e) {
            System.out.println("Error loading servlet " + className + ": " + e.getMessage());
        }

    }

    /**
     * Loads an annotated servlet from the folder into the container's hash table.
     * External name is read from the @URLServletName annotation.
     *
     * @param name the servlet's internal class file name
     */
    private void loadAnnotatedServlet(String name) {


        try {
            URL[] a = new URL[]{new File(tpbsca.DYNAMIC_ROOT + File.separator + "class").toURI().toURL()};
            URLClassLoader loader = new URLClassLoader(a);

            if(name.trim().endsWith(".class"))
                name = name.split(".class")[0];

            Class<?> servletClass = loader.loadClass(name);

            String externalName = servletClass.getAnnotation(URLServletName.class).value();

            if(tpbsca.servletHashtable.containsKey(externalName)) {
                System.out.println("Servlet " + name + " is already loaded as \"" + externalName + "\".");
                return;
            }

            if(externalName.isEmpty()) {
                externalName = name;
                System.out.println("Servlet annotated name is blank. Will be loaded using class name instead.");
            }

            tpbsca.servletHashtable.put(externalName, (HttpServlet) servletClass.getDeclaredConstructor().newInstance());
            System.out.println("Servlet " + name + " successfully loaded as \"" + externalName + "\".");

            loader.close();
        }

        catch (ClassNotFoundException e) {
         System.out.println(name + " is not present in the annotated servlets repository.");
        }
        catch (Exception e) {
            System.out.println("Error loading servlet " + name + ": " + e.getMessage());
        }

    }

    /**
     * Unloads a currently loaded servlet, referred to by its external name.
     * Tip: use the list / listloaded command to see valid names for this command.
     *
     * @param name The servlet's external name.
     */
    void unloadServlet(String name) {

        if(!tpbsca.servletHashtable.containsKey(name)) {
            System.out.println(name + " is not loaded.");
            return;
        }

        tpbsca.servletHashtable.remove(name);
        System.out.println(name + " unloaded successfully.");
    }

    private void execute(String cmd) {

        cmd = cmd.trim();
        String[] words = cmd.split(" ");

        switch (words[0]) {
            
            case "load": {
                if(words.length < 2)
                    System.out.println("No name provided. Usage: load <servlet name>");
                else loadServlet(words[1]);
                return;
            }

            case "remove": {
                if(words.length < 2)
                    System.out.println("No name provided. Usage: remove <servlet name>");
                else unloadServlet(words[1]);
                return;
            }

            case "lwa":
            case "load-with-annotations": {
                if(words.length < 2)
                    System.out.println("No name provided. Usage: load-with-annotations <servlet name>");
                else loadAnnotatedServlet(words[1]);
                return;
            }

            case "la":
            case "listavailable": {
                listAvailableServlets();
                return;
            }

            case "list":
            case "listloaded": {
                listLoadedServlets();
                return;
            }

            case "quit" : return;

            default:
                System.out.println(
                        "Unknown command: " + cmd +
                        "\nSupported commands are:\nload <servlet>, load-with-annotations <servlet> (lwa),\n" +
                        "remove <servlet>, listloaded (list), listavailable (la), quit.\n");
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
                System.out.println("Error while parsing command.");
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }

        } while (command != null && !command.equals("quit") && !services.isShutdown());

        services.shutdown();
    }

}
