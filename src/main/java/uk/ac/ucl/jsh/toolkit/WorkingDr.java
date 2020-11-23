package uk.ac.ucl.jsh.toolkit;


public final class WorkingDr{
    private static final WorkingDr INSTANCE = new WorkingDr(System.getProperty("user.dir"));
     
    private String WD;
 
    private WorkingDr(String workingDirectoryPath) {
        this.setWD(workingDirectoryPath);
    }

    public static WorkingDr getInstance() {
        return INSTANCE;
    }

    public String getWD() {
        return this.WD;
    }

    public void setWD(String workingDirectoryPath) {
        this.WD = workingDirectoryPath;
    }


    
}