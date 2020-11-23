package uk.ac.ucl.jsh.applications;


import java.util.Map;

import java.util.HashMap;
import java.lang.String;


public class AppFactory {
    private final Map<String, Application> appMap;

    public AppFactory(){
        appMap = new HashMap<String, Application>(2);
        appMap.put("cd",new Cd());
        appMap.put("cat",new Cat());
        appMap.put("call",new Call());
        appMap.put("cut",new Cut());
        appMap.put("echo",new Echo());
        appMap.put("find",new Find());
        appMap.put("grep",new Grep());
        appMap.put("head",new Head());
        appMap.put("ls",new Ls());
        appMap.put("pipe",new Pipe());
        appMap.put("pwd",new Pwd());
        appMap.put("sort",new Sort());
        appMap.put("tail",new Tail());
        appMap.put("uniq",new Uniq());
    }

    public Application buildApp(final String appType) {
        return appMap.get(appType);
    }


    // public Application getApplication(String applicationType){
    //     if(applicationType == null){
    //         return null;
    //     }
    //     if(applicationType.equalsIgnoreCase("cd")){
    //         return new Cd();
    //     }
    //     else if(applicationType.equalsIgnoreCase("pwd")){
    //         return new Pwd();
    //     }
    //     else if(applicationType.equalsIgnoreCase("ls")){
    //         return new Ls();
    //     }
    //     else if(applicationType.equalsIgnoreCase("cat")){
    //         return new Cat();
    //     }
    //     else if(applicationType.equalsIgnoreCase("echo")){
    //         return new Echo();
    //     }
    //     else if(applicationType.equalsIgnoreCase("head")){
    //         return new Head();
    //     }
    //     else if(applicationType.equalsIgnoreCase("tail")){
    //         return new Tail();
    //     }
    //     else if(applicationType.equalsIgnoreCase("grep")){
    //         return new Grep();
    //     }
    //     else if(applicationType.equalsIgnoreCase("cut")){
    //         return new Cut();
    //     }
    //     else if(applicationType.equalsIgnoreCase("find")){
    //         return new Find();
    //     }
    //     else if(applicationType.equalsIgnoreCase("uniq")){
    //         return new Uniq();
    //     }
    //     else if(applicationType.equalsIgnoreCase("sort")){
    //         return new Sort();
    //     }
    //     return null;
    // }     
}