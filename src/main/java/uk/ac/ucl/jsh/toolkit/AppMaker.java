package uk.ac.ucl.jsh.toolkit;
import uk.ac.ucl.jsh.applications.*;

public class AppMaker {

    private static final AppMaker INSTANCE = new AppMaker();

    private static AppFactory appFactory = new AppFactory();

    public static AppMaker getInstance() {
        return INSTANCE;
    }

    public Application makeApp(String appName) throws JshException {
        Boolean appIsUnsafe = false;
        if (appName.startsWith("_")) {
            appIsUnsafe = true;
            appName = appName.replaceFirst("_", "");
        }
        Application app = appFactory.getApplication(appName);
        if (appIsUnsafe) {
            return new Unsafe(app);
        }
        else{
            return app;
        }
    }  
}