package uk.ac.ucl.jsh.applications;

import java.util.Map;

import uk.ac.ucl.jsh.toolkit.JshException;

import java.util.HashMap;
import java.lang.String;


public class AppFactory {
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

    public Application getApplication(String appType) throws JshException {
        Application app;
        if ((app = appMap.get(appType.toLowerCase())) == null){
            throw new JshException(appType + " is not an application");
        }
        return app;
    }
}