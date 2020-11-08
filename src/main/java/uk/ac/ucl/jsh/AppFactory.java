package uk.ac.ucl.jsh;


public class AppFactory {
    public Application getApplication(String applicationType){
        if(applicationType == null){
            return null;
        }
        if(applicationType.equalsIgnoreCase("cd")){
            return new Cd();
        }
        else if(applicationType.equalsIgnoreCase("pwd")){
            return new Pwd();
        }
        else if(applicationType.equalsIgnoreCase("ls")){
            return new Ls();
        }
        else if(applicationType.equalsIgnoreCase("cat")){
            return new Cat();
        }
        else if(applicationType.equalsIgnoreCase("echo")){
            return new Echo();
        }
        else if(applicationType.equalsIgnoreCase("head")){
            return new Head();
        }
        else if(applicationType.equalsIgnoreCase("tail")){
            return new Tail();
        }
        else if(applicationType.equalsIgnoreCase("grep")){
            return new Grep();
        }
        else if(applicationType.equalsIgnoreCase("cut")){
            return new Cut();
        }
        else if(applicationType.equalsIgnoreCase("find")){
            return new Find();
        }
        else if(applicationType.equalsIgnoreCase("uniq")){
            return new Uniq();
        }
        else if(applicationType.equalsIgnoreCase("sort")){
            return new Sort();
        }
        return null;
    }     
}