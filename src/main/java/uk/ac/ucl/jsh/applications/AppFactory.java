package uk.ac.ucl.jsh.applications;

import java.util.Map;

import uk.ac.ucl.jsh.toolkit.JshException;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import java.lang.String;


public class AppFactory {
    private static final Object Application = null;
    private final Map<String, Application> appMap;

    public AppFactory(){
        appMap = new HashMap<String, Application>(2);
        appMap.put("cd",new Cd());
        appMap.put("cat",new Cat());
        appMap.put("sort",new Sort());
        appMap.put("cut",new Cut());
        appMap.put("echo",new Echo());
        appMap.put("find",new Find());
        appMap.put("grep",new Grep());
        appMap.put("head",new Head());
        appMap.put("ls",new Ls());
        appMap.put("pwd",new Pwd());
        appMap.put("sort",new Sort());
        appMap.put("tail",new Tail());
        appMap.put("uniq",new Uniq());
    }

    public Application buildApp(final String appType) {
        return appMap.get(appType.toLowerCase());
    }
    public final void checkAppArguements(ArrayList<String> appArgs, InputStream input) throws JshException{
        if(appArgs.isEmpty() && input == null){
            throw new JshException("missing InputStream");
        }
		if (!appMap.containsKey(Application)){
            throw new JshException("incorrect InputStream");
        }    

    public Application getApplication(final String appType) {
        return appMap.get(appType);
    }
}