package uk.ac.ucl.jsh.toolkit;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.ac.ucl.jsh.applications.*;

/**
 * The class AppExecutor follows the singleton pattern, it receives the name of
 * requested application from the parsed command line and creates and executes
 * the corresponding safe or unsafe application
 */
public class AppExecutor {

    /**
     * the reference to the single AppMaker
     */
    private static final AppExecutor INSTANCE = new AppExecutor();

    /**
     * the reference to the single AppFactory
     */
    private static AppFactory appFactory = new AppFactory();

    /**
     * a methods to get the single AppMaker instance
     * 
     * @return the single AppMaker instance
     */
    public static AppExecutor getInstance() {
        return INSTANCE;
    }

    /**
     * a methods that takes the token from parsed command line, and creates the
     * requested application
     * 
     * @param appName The token from parsed command line, which specifies the
     *                requested application
     * @return Application The application requested
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    private Application makeApp(String appName) throws JshException {
        Boolean appIsUnsafe = false;
        if (appName.startsWith("_")) {
            appIsUnsafe = true;
            appName = appName.replaceFirst("_", "");
        }
        Application app = appFactory.getApplication(appName);
        return appIsUnsafe ? new Unsafe(app) : app;
    }

    /**
     * The method to execute a certain application specified by the command line
     * token
     * 
     * @param appName The token from parsed command line, which indicates which
     *                application to be created
     * @param args    The arguments for the application
     * @param input   The stream where the application reads the input
     * @param output  The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    public void executeApp(String appName, ArrayList<String> args, InputStream input, OutputStream output)
            throws JshException {
        makeApp(appName).exec(args, input, output);
    }
}